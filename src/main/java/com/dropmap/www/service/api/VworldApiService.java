package com.dropmap.www.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class VworldApiService {

    @Value("${server.name}")
    private String serverName;

    @Value("${VWORLD.API.KEY}")
    private String vworldApiKey;

    @Value("${VWORLD.API.URL}")
    private String vworldApiURL;

    private final RestTemplate restTemplate;

    private static final int MAX_RETRY_COUNT = 10;
    private static final long RETRY_DELAY_MILLIS = 2000;

    public String getBoundary(String coords, int depth) throws JsonProcessingException {

        int retryCount = 0;
        String lot = coords.split(",")[0];
        String lat = coords.split(",")[1];
        String[] dataArr = {"LT_C_ADSIDO_INFO","LT_C_ADSIGG_INFO","LT_C_ADEMD_INFO"};//시,구,동

        if(depth > 2){
            return null;
        }

        //API 호출
        URI apiUri = UriComponentsBuilder.fromUriString(vworldApiURL)
                .queryParam("key",vworldApiKey)
                .queryParam("domain",serverName)
                .queryParam("request","GetFeature")
                .queryParam("data",dataArr[depth])
                .queryParam("geomfilter","POINT("+lot + " " + lat+")")
                .encode()
                .build()
                .toUri();

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(apiUri, HttpMethod.GET, getHttpEntity(), String.class);
        } catch (HttpClientErrorException e) {
            //에러처리
            apiErrorHandler(response,apiUri,retryCount);
            //TODO TEST
//            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
//                // 400 에러 처리
//                String body = e.getResponseBodyAsString();
//                System.out.println("400 에러 발생: " + body);
//            }
        }

        //데이터파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String coordinates = jsonNode.path("response").path("result").path("featureCollection").path("features").get(0).path("geometry").path("coordinates").get(0).get(0).toString();
        return coordinates;
    }

    private HttpEntity<String> getHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
