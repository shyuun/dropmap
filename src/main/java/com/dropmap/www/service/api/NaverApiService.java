package com.dropmap.www.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverApiService {

    @Value("${NAVER.CLOUD.API.KEY}")
    private String naverApiKey;

    @Value("${NAVER.CLOUD.API.SECRET}")
    private String naverApiSecret;

    @Value("${NAVER.CLOUD.API.GEOCODING.URL}")
    private String naverApiGeocodingUrl;

    @Value("${NAVER.CLOUD.API.REVERSE.GEOCODING.URL}")
    private String naverApiReverseGeocodingUrl;

    private final RestTemplate restTemplate;

    private static final int MAX_RETRY_COUNT = 10;
    private static final long RETRY_DELAY_MILLIS = 2000;

    public List<Map<String, String>> getAddress(Set<String> coords) {

        //좌표를 정제된 주소,좌표의 리스트로 전환
        List<Map<String,String>> addressList = coords.parallelStream()
                .filter(coord -> !coord.trim().equals(","))
                .map(coord -> {
                    try {
                        log.info("Coord: {}, Thread: {}", coord, Thread.currentThread().getName());
                        Map<String, String> resultMap = new HashMap<>();
                        String area1 = "",area2 = "",area3 = "",area4 = "";
                        String roadAddress = "",lotAddress = "";
                        int retryCount = 0;

                        //API 호출
                        URI apiUri = UriComponentsBuilder.fromUriString("https://"+naverApiReverseGeocodingUrl)
                                .queryParam("output","json")
                                .queryParam("coords",coord)
                                .queryParam("orders","legalcode,admcode,addr,roadaddr")/*법정동,행정동,지번,도로명*/
                                .build(true)
                                .toUri();

                        ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, getHttpEntity(), String.class);

                        //에러처리
                        apiErrorHandler(response,apiUri,retryCount);

                        //데이터 파싱
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = objectMapper.readTree(response.getBody());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        String resultCode = jsonNode.path("status").path("code").asText("");
                        String resultMsg = jsonNode.path("status").path("message").asText("");

                        if(!"0".equals(resultCode)){
                            //비정상
                            //throw new IllegalArgumentException("["+resultCode+"] " + resultMsg);
                            log.warn("[getAddress] Invalid response for coord {}: {} - {}", coord, resultCode, resultMsg);
                            return null; // or skip
                        }

                        JsonNode resultsNode = jsonNode.path("results");
                        for (JsonNode result : resultsNode) {
                            String name = result.path("name").asText();//legalcode,admcode,addr,roadaddr

                            if("legalcode".equals(name)){//법정동
                                area1 = result.path("region").path("area1").path("name").asText();//서울특별시
                                area2 = result.path("region").path("area2").path("name").asText();//강남구
                                area3 = result.path("region").path("area3").path("name").asText();//역삼동
                            } else if ("admcode".equals(name)){//행정동
                                area4 = result.path("region").path("area3").path("name").asText();//역삼 2동
                            } else if ("addr".equals(name)){//지번 주소
                                lotAddress = result.path("region").path("area1").path("name").asText();//서울특별시
                                lotAddress += " " + result.path("region").path("area2").path("name").asText();//강남구
                                lotAddress += " " + result.path("region").path("area3").path("name").asText();//역삼동
                                lotAddress += " " + result.path("land").path("number1").asText();//719
                                if(!"".equals(result.path("land").path("number2").asText(""))){
                                    lotAddress += "-" + result.path("land").path("number2").asText("");//24
                                }
                            } else if ("roadaddr".equals(name)){//도로명 주소
                                roadAddress = result.path("region").path("area1").path("name").asText();//서울특별시
                                roadAddress += " " + result.path("region").path("area2").path("name").asText();//강남구
                                roadAddress += " " + result.path("region").path("area3").path("name").asText();//역삼동
                                roadAddress += " " + result.path("land").path("name").asText();//85길
                                roadAddress += " " + result.path("land").path("number1").asText();//32
                            }
                        }

                        String lot = coord.split(",")[0];
                        String lat = coord.split(",")[1];

                        resultMap.put("lot",lot);
                        resultMap.put("lat",lat);
                        resultMap.put("area0","대한민국");
                        resultMap.put("area1",area1);
                        resultMap.put("area2",area2);
                        resultMap.put("area3",area3);
                        resultMap.put("area4",area4);
                        resultMap.put("lotAddress",lotAddress);
                        resultMap.put("roadAddress",roadAddress);

                        if(!"서울특별시".equals(area1)){
                            return null;
                        }

                        return resultMap;
                    } catch (Exception e) {
                        log.error("Error during address fetch for coord {}: {}", coord, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return addressList;
    }

    public String getCoord(String address) {

        //단일 주소를 좌표로 전환
        int retryCount = 0;
        String lot = "";
        String lat = "";

        try {

            if (!address.trim().isEmpty()) {
                //API호출
                URI apiUri = UriComponentsBuilder.fromUriString("https://" + naverApiGeocodingUrl)
                        .queryParam("query", address)
                        .encode()
                        .build()
                        .toUri();

                ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, getHttpEntity(), String.class);

                //에러처리
                apiErrorHandler(response, apiUri, retryCount);

                //데이터 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;

                try {
                    jsonNode = objectMapper.readTree(response.getBody());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                String resultStatus = jsonNode.path("status").asText();
                String resultMsg = jsonNode.path("errorMessage").asText();
                int resultTotalCount = jsonNode.path("meta").path("totalCount").asInt(0);

                if (!"OK".equals(resultStatus)) {
                    //비정상
                    //throw new IllegalArgumentException("["+resultStatus+"] " + resultMsg);
                    log.warn("[getAddress] Invalid response for coord {}: {} - {}", address, resultStatus, resultMsg);
                }

                if (resultTotalCount < 1) {
                    return null;
                }

                JsonNode resultsNode = jsonNode.path("addresses");
                if (resultsNode != null) {
                    lot = resultsNode.get(0).path("x").asText();
                    lat = resultsNode.get(0).path("y").asText();
                }

                if ("".equals(lot) || "".equals(lat)) {
                    return null;
                }
            }

            return lot + "," + lat;
        } catch (Exception e) {
            log.error("safeGetCoord 실패: {}", address);
            return null;
        }
    }

    private HttpEntity<String> getHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-api-key-id",naverApiKey);
        headers.set("x-ncp-apigw-api-key",naverApiSecret);
        return new HttpEntity<>(headers);
    }

    private void apiErrorHandler(ResponseEntity<String> response, URI apiUri, int retryCount){
        if(response.getStatusCode().is4xxClientError()){
            //exception
            throw new IllegalArgumentException("잘못된 요청입니다. 파라미터를 확인하세요.");
        } else if(response.getStatusCode().is5xxServerError()){
            //retry
            while(retryCount < MAX_RETRY_COUNT){
                response = restTemplate.exchange(apiUri, HttpMethod.GET, getHttpEntity(), String.class);
                if(response.getStatusCode().is5xxServerError()){
                    retryCount++;
                    try {
                        Thread.sleep(RETRY_DELAY_MILLIS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    break;
                }
            }
        }
    }
}
