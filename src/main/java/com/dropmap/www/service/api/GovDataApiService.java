package com.dropmap.www.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GovDataApiService {

    private final NaverApiService naverApiService;

    @Value("${DATA.GO.KR.DROP.OPENAPI.BASEURL}")
    private String openApiBaseUrl;

    @Value("${DATA.GO.KR.DROP.FILEDATA.SWAGGERURL}")
    private String fileDataSwaggerUrl;

    @Value("${DATA.GO.KR.DROP.FILEDATA.BASEURL}")
    private String fileDataBaseUrl;

    @Value("${DATA.GO.KR.ENC.APIKEY}")
    private String openApiKey;

    private final RestTemplate restTemplate;

    private static final int MAX_RETRY_COUNT = 10;
    private static final long RETRY_DELAY_MILLIS = 2000;
    private final List<String> ABNORMAL_LIST = new ArrayList<>();
    private int totalDataCount = 0;
    private int errorDataCount = 0;
    private int fixDataCount = 0;

    public Set<String> getOpenApiCoords() throws JsonProcessingException, InterruptedException {
        Set<String> coordsSet = new HashSet<>();
        int retryCount = 0;
        String coordStr = "";
        String address = "";

        //API호출
        URI apiUri = UriComponentsBuilder.fromUriString(openApiBaseUrl)
                .queryParam("serviceKey",openApiKey)
                .queryParam("pageNo","0")
                .queryParam("numOfRows","2000")
                .queryParam("type","JSON")
                .queryParam("CTPV_NM", URLEncoder.encode("서울특별시", StandardCharsets.UTF_8))
                .build(true)
                .toUri();

        ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

        //에러처리
        if(response.getStatusCode().is4xxClientError()){
            //exception
            throw new IllegalArgumentException("잘못된 요청입니다. 파라미터를 확인하세요.");
        } else if(response.getStatusCode().is5xxServerError()){
            //retry
            while(retryCount < MAX_RETRY_COUNT){
                response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);
                if(response.getStatusCode().is5xxServerError()){
                    retryCount++;
                    Thread.sleep(RETRY_DELAY_MILLIS);
                } else {
                    break;
                }
            }
        }

        //데이터 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String resultCode = jsonNode.path("response").path("header").path("resultCode").asText();
        String resultMsg = jsonNode.path("response").path("header").path("resultMsg").asText("API 응답에 실패했습니다.");

        if(!"00".equals(resultCode)){
            //비정상
            throw new IllegalArgumentException("["+resultCode+"] " + resultMsg);
        }

        JsonNode dataArr = jsonNode.path("response").path("body").path("items");

        totalDataCount += jsonNode.path("response").path("body").path("totalCount").asInt(0);//데이터 토탈카운트 추가

        for(JsonNode data : dataArr){
            String lat = data.path("lat").asText("");
            String lot = data.path("lot").asText("");

            if("".equals(lat) || "".equals(lot)){
                ABNORMAL_LIST.add(jsonNode.asText());//이상 데이터 add
                errorDataCount++;//데이터 에러카운트 추가
                Iterator<Map.Entry<String, JsonNode>> fields = data.fields();

                while(fields.hasNext()){
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String key = entry.getKey();

                    if(key.contains("Addr")){
                        address = entry.getValue().asText();
                        String coord = naverApiService.getCoord(address);//주소 -> 위경도 변경 lot,lat 형식

                        if(coord != null){
                            coordsSet.add(coord);
                            fixDataCount++;
                            break;
                        }
                    }
                }
            } else {
                coordStr = lot + "," + lat;
                coordsSet.add(coordStr);
            }
        }

        return coordsSet;
    }

    public List<String> getFileDataApiUrls() {
        List<String> urls = new ArrayList<>();
        return urls;
    }

    public Set<String> getFileDataApiCoords(List<String> fileDataUrlList) {
        Set<String> coordsSet = new HashSet<>();
        return coordsSet;
    }
}
