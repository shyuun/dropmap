package com.dropmap.www;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class DropmapApiTests {

    /**  API 목록
     *  1.의류수거함 표준데이터
     *  2.의류수거함 SWAGGER
     *  3.의류수거함 지자체 데이터
     *  4.네이버 지오코딩
     *  5.네이버 리버스 지오코딩
     *  6.vworld 경계데이터
     */

    @Autowired
    private RestTemplate restTemplate;

    @Value("${DATA.GO.KR.DROP.OPENAPI.BASEURL}")
    private String openApiBaseUrl;

    @Value("${DATA.GO.KR.DROP.FILEDATA.SWAGGERURL}")
    private String fileDataSwaggerUrl;

    @Value("${DATA.GO.KR.DROP.FILEDATA.BASEURL}")
    private String fileDataBaseUrl;

    @Value("${DATA.GO.KR.ENC.APIKEY}")
    private String openApiKey;

    @Value("${NAVER.CLOUD.API.KEY}")
    private String naverApiKey;

    @Value("${NAVER.CLOUD.API.SECRET}")
    private String naverApiSecret;

    @Value("${NAVER.CLOUD.API.GEOCODING.URL}")
    private String naverApiGeocodingUrl;

    @Value("${NAVER.CLOUD.API.REVERSE.GEOCODING.URL}")
    private String naverApiReverseGeocodingUrl;

    @Value("${VWORLD.API.KEY}")
    private String vworldApiKey;

    @Value("${VWORLD.API.URL}")
    private String vworldApiURL;

    @Test
    public void 의류수거함_표준데이터_API_응답이된다() throws JsonProcessingException {
        //GIVEN
        URI apiUri = UriComponentsBuilder.fromUriString(openApiBaseUrl)
                .queryParam("serviceKey",openApiKey)
                .queryParam("pageNo","0")
                .queryParam("numOfRows","2000")
                .queryParam("type","JSON")
                .queryParam("CTPV_NM", URLEncoder.encode("서울특별시", StandardCharsets.UTF_8))
                .build(true)
                .toUri();

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());//200인지 확인
        assertNotNull(response.getBody());//response JSON인지 확인

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        assertTrue(jsonNode.path("response").path("body").has("items"));//data필드 존재하는지 확인
    }

    @Test
    public void 지자체데이터_SWAGGER_API_응답이된다() throws JsonProcessingException {
        //GIVEN
        List<String> ids = new ArrayList<>(Arrays.asList("15127131", "15137988", "15138051", "15127065",
                "15076398", "15109594", "15068871", "15106679",
                "15112228", "15068021", "15068863", "15126958",
                "15127036", "15127100", "15105196", "15106473",
                "15104622", "15127304"));

        ids.stream().forEach(id -> {
            String swaggerUrl = fileDataSwaggerUrl.replace("@ID@", ids.get(0));

            URI apiUri = UriComponentsBuilder.fromUriString(swaggerUrl)
                    .build(true)
                    .toUri();

            //WHEN
            ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

            //THEN
            assertEquals(HttpStatus.OK, response.getStatusCode());//200인지 확인
            assertNotNull(response.getBody());//response JSON인지 확인

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
            }

            assertNotNull(latestPath);//data필드 존재하는지 확인
        });
    }

    @Test
    public void 지자체데이터_API_응답이된다() throws JsonProcessingException {
        //GIVEN
        List<String> urls = new ArrayList<>();

        List<String> ids = new ArrayList<>(Arrays.asList("15127131", "15137988", "15138051", "15127065",
                "15076398", "15109594", "15068871", "15106679",
                "15112228", "15068021", "15068863", "15126958",
                "15127036", "15127100", "15105196", "15106473",
                "15104622", "15127304"));

        ids.stream().forEach(id -> {
            String swaggerUrl = fileDataSwaggerUrl.replace("@ID@", ids.get(0));

            URI apiUri = UriComponentsBuilder.fromUriString(swaggerUrl)
                    .build(true)
                    .toUri();

            ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

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
                urls.add(latestPath);
            }
        });

        urls.stream().forEach(url -> {
            URI apiUri = UriComponentsBuilder.fromUriString(fileDataBaseUrl + url)
                    .queryParam("serviceKey",openApiKey)
                    .queryParam("page","0")
                    .queryParam("perPage","1000")
                    .queryParam("returnType","JSON")
                    .build(true)
                    .toUri();

            //WHEN
            ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, null, String.class);

            //THEN
            assertEquals(HttpStatus.OK, response.getStatusCode());//200인지 확인
            assertNotNull(response.getBody());//response JSON인지 확인

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            JsonNode dataArr = jsonNode.path("data");
            int totalCount = jsonNode.path("totalCount").asInt(0);//data필드 존재하는지 확인
            assertTrue(totalCount > 0);
        });

    }

    @Test
    public void 네이버_지오코딩_API_응답이된다() throws JsonProcessingException {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id",naverApiKey);
        headers.set("x-ncp-apigw-api-key",naverApiSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String address = "공릉동 569-21";

        URI apiUri = UriComponentsBuilder.fromUriString("https://"+naverApiGeocodingUrl)
                .queryParam("query",address)
                .encode()
                .build()
                .toUri();

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, entity, String.class);

        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());//200인지 확인
        assertNotNull(response.getBody());//response JSON인지 확인

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        int totalCount = jsonNode.path("meta").path("totalCount").asInt(0);
        assertTrue(totalCount > 0);//data필드 존재하는지 확인
    }

    @Test
    public void 네이버_리버스_지오코딩_API_응답이된다() throws JsonProcessingException {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-ncp-apigw-api-key-id",naverApiKey);
        headers.set("x-ncp-apigw-api-key",naverApiSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String coord = "126.9306461,37.60630915";

        URI apiUri = UriComponentsBuilder.fromUriString("https://"+naverApiReverseGeocodingUrl)
                .queryParam("coords",coord)
                .queryParam("output","json")
                .queryParam("orders","legalcode,admcode,addr,roadaddr")/*법정동,행정동,지번,도로명*/
                .build(true)
                .toUri();

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, entity, String.class);

        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());//200인지 확인
        assertNotNull(response.getBody());//response JSON인지 확인

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String resultName = jsonNode.path("status").path("name").asText();
        assertEquals("ok",resultName);//상태값 정상인지 확인
    }

    @Test
    public void VWORLD_API_응답이된다() throws JsonProcessingException {
        //GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String serverName = "127.0.0.1";
        String lot = "126.9306461";
        String lat = "37.60630915";
        String[] dataArr = {"LT_C_ADSIDO_INFO","LT_C_ADSIGG_INFO","LT_C_ADEMD_INFO"};

        for(String data : dataArr){

            URI apiUri = UriComponentsBuilder.fromUriString(vworldApiURL)
                    .queryParam("key",vworldApiKey)
                    .queryParam("domain",serverName)
                    .queryParam("request","GetFeature")
                    .queryParam("data",data)
                    .queryParam("geomfilter","POINT("+lot + " " + lat+")")
                    .encode()
                    .build()
                    .toUri();

            //WHEN
            ResponseEntity<String> response = restTemplate.exchange(apiUri, HttpMethod.GET, entity, String.class);

            //THEN
            assertEquals(HttpStatus.OK, response.getStatusCode());//200인지 확인
            assertNotNull(response.getBody());//response JSON인지 확인

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            int totalCount = jsonNode.path("response").path("record").path("total").asInt(0);
            assertTrue(totalCount > 0);//data필드 존재하는지 확인
        }
    }
}
