package com.dropmap.www.service.api;

import com.dropmap.www.domain.source.SourceInfo;
import com.dropmap.www.domain.source.SourceInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
    private final SourceInfoRepository sourceInfoRepository;

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
    final double SEOUL_LAT_MIN = 37.413294;
    final double SEOUL_LAT_MAX = 37.715133;
    final double SEOUL_LOT_MIN = 126.734086;
    final double SEOUL_LOT_MAX = 127.269311;
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
        apiErrorHandler(response,apiUri,retryCount);

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

            if(!"".equals(lat) && !"".equals(lot)){
                coordStr = lot + "," + lat;
                coordsSet.add(coordStr);
            } else {
                //비정상 데이터 처리
                String coord = handleMissingCoordnates(data,"Addr");

                if(coord != null){
                    coordsSet.add(coord);
                }
            }
        }

        return coordsSet;
    }

    public List<String> getFileDataApiUrls() {
        List<String> urlList = new ArrayList<>();
        List<String> types = List.of("A", "B");
        List<SourceInfo> sourceInfoList = sourceInfoRepository.findBySourceTypeInAndUptYnAndSourceCodeIsNotNull(types, "N");//데이터 업데이트 확인

        sourceInfoList.forEach(sourceInfo -> {
            int retryCount = 0;
            String swaggerUrl = fileDataSwaggerUrl.replace("@ID@", sourceInfo.getSourceCode());

            //API호출
            URI apiUri = UriComponentsBuilder.fromUriString(swaggerUrl)
                    .build(true)
                    .toUri();

            ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

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

            JsonNode pathNode = jsonNode.path("paths");
            String latestPath = null;

            Iterator<String> fieldNames = pathNode.fieldNames();
            while (fieldNames.hasNext()) {
                latestPath = fieldNames.next();
                urlList.add(latestPath);
            }
        });

        return urlList;
    }

    public Set<String> getFileDataApiCoords(List<String> fileDataUrlList) {
        Set<String> coordsSet = new HashSet<>();

        fileDataUrlList.stream().forEach(url -> {
            int retryCount = 0;
            String address = "";

            //API 호출
            URI apiUri = UriComponentsBuilder.fromUriString(fileDataBaseUrl + url)
                    .queryParam("serviceKey",openApiKey)
                    .queryParam("page","0")
                    .queryParam("perPage","1000")
                    .queryParam("returnType","JSON")
                    .build(true)
                    .toUri();

            ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

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
            JsonNode dataArr = jsonNode.path("data");
            int totalCount = jsonNode.path("totalCount").asInt(0);//data필드 존재하는지 확인

            for(JsonNode data : dataArr){
                String lat = data.path("위도").asText("").replaceAll("[^0-9eE.\\-]", "");
                String lot = data.path("경도").asText("").replaceAll("[^0-9eE.\\-]", "");

                //위경도 데이터 없을경우 주소->위경도로 다시변환
                if(lat.isEmpty() || lot.isEmpty()){
                    //이상 데이터 add
                    String coord = handleMissingCoordnates(data,"주소");
                    if(coord != null){
                        coordsSet.add(coord);
                    }
                    continue;
                }

                //서울시에 속하는 위경도인지 확인
                if(Math.abs(Double.parseDouble(lat)) <= 1e-8 || Math.abs(Double.parseDouble(lot)) <= 1e-8
                        ||Double.parseDouble(lat) < SEOUL_LAT_MIN || Double.parseDouble(lat) > SEOUL_LAT_MAX ||
                        Double.parseDouble(lot) < SEOUL_LOT_MIN || Double.parseDouble(lot) > SEOUL_LOT_MAX){
                    //이상 데이터 add
                    String coord = handleMissingCoordnates(data,"주소");
                    if(coord != null){
                        coordsSet.add(coord);
                    }
                    continue;
                }

                //정상의 경우
                coordsSet.add(lot + "," + lat);
            }
        });

        return coordsSet;
    }

    private void apiErrorHandler(ResponseEntity<String> response, URI apiUri, int retryCount){
        if(response.getStatusCode().is4xxClientError()){
            //exception
            throw new IllegalArgumentException("잘못된 요청입니다. 파라미터를 확인하세요.");
        } else if(response.getStatusCode().is5xxServerError()){
            //retry
            while(retryCount < MAX_RETRY_COUNT){
                response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);
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

    private String handleMissingCoordnates(JsonNode data, String addressName){
        ABNORMAL_LIST.add(data.asText());
        errorDataCount++;//데이터 에러카운트 추가
        Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
        String address = "";
        String coord = null;

        while(fields.hasNext()){
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();

            if(key.contains(addressName)){
                address = entry.getValue().asText();
                coord = naverApiService.getCoord(address);//주소 -> 위경도 변경 lot,lat 형식

                if(coord != null){
                    fixDataCount++;//데이터 수정카운트 추가
                    return coord;
                }
            }
        }

        return coord;
    }
}
