package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.domain.source.SourceInfoRepository;
import com.dropmap.www.domain.unstructured.UnstructuredDataInfoRepository;
import com.dropmap.www.service.api.GovDataApiService;
import com.dropmap.www.service.api.NaverApiService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DataClearService {

    private final EntityManager entityManager;
    private final DistrictInfoRepository districtInfoRepository;
    private final GeolocationInfoRepository geolocationInfoRepository;
    private final SourceInfoRepository sourceInfoRepository;

    @Transactional
    protected void clearDatabase() {
        sourceInfoRepository.resetUptYnAll();
        districtInfoRepository.deleteAllInBatch();
        geolocationInfoRepository.deleteAllInBatch();
        entityManager.flush();
        entityManager.clear();
    }
}
