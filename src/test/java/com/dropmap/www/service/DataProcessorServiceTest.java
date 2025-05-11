package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
