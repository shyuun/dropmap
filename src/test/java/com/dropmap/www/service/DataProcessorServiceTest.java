package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class DataProcessorServiceTest {

    @Autowired
    private DataProcessorService dataProcessorService;

    @Autowired
    private DistrictInfoRepository districtInfoRepository;

    @Autowired
    private GeolocationInfoRepository geolocationInfoRepository;

    @Test
    void 데이터_삭제가_정상적으로된다(){
        dataProcessorService.clearDatabase();

        assertEquals(0, districtInfoRepository.count());
        assertEquals(0, geolocationInfoRepository.count());
    }

    @Test
    void OPENAPI_저장이_정상적으로된다() throws JsonProcessingException, InterruptedException {
        dataProcessorService.clearDatabase();

        assertEquals(0, districtInfoRepository.count());
        assertEquals(0, geolocationInfoRepository.count());

        dataProcessorService.insertOpenApiData();

        assertTrue(districtInfoRepository.count() > 0);
        assertTrue(geolocationInfoRepository.count() > 0);
    }

    @Test
    void 지자체데이터_저장이_정상적으로된다() throws JsonProcessingException, InterruptedException {
        dataProcessorService.clearDatabase();

        assertEquals(0, districtInfoRepository.count());
        assertEquals(0, geolocationInfoRepository.count());

        dataProcessorService.insertFileApiData();

        assertTrue(districtInfoRepository.count() > 0);
        assertTrue(geolocationInfoRepository.count() > 0);
    }

    @Test
    void 비형식데이터_저장이_정상적으로된다() throws JsonProcessingException, InterruptedException {
        dataProcessorService.clearDatabase();

        assertEquals(0, districtInfoRepository.count());
        assertEquals(0, geolocationInfoRepository.count());

        dataProcessorService.insertUnstructuredData();

        assertTrue(districtInfoRepository.count() > 0);
        assertTrue(geolocationInfoRepository.count() > 0);
    }
}
