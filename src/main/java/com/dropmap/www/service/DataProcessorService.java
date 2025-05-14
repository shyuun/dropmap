package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.domain.source.SourceInfoRepository;
import com.dropmap.www.domain.unstructured.UnstructuredDataInfoRepository;
import com.dropmap.www.service.api.GovDataApiService;
import com.dropmap.www.service.api.NaverApiService;
import com.dropmap.www.template.DataInitializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DataProcessorService extends DataInitializer {

    private final EntityManager entityManager;
    private final DistrictInfoRepository districtInfoRepository;
    private final GeolocationInfoRepository geolocationInfoRepository;
    private final SourceInfoRepository sourceInfoRepository;
    private final UnstructuredDataInfoRepository unstructuredDataInfoRepository;
    private final GovDataApiService govDataApiService;
    private final NaverApiService naverApiService;
    private final DataInsertSerivce dataInsertService;
    private final UnstructuredDataService unstructedDataService;

    @Override
    @Transactional
    public void init() throws JsonProcessingException, InterruptedException {
        clearDatabase();
        insertOpenApiData();
        insertFileApiData();
        insertUnstructuredData();
        govDataApiService.printStatLog();
    }

    @Override
    protected void clearDatabase() {
        sourceInfoRepository.resetUptYnAll();
        districtInfoRepository.deleteAllInBatch();
        geolocationInfoRepository.deleteAllInBatch();
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    protected void insertOpenApiData() throws JsonProcessingException, InterruptedException {
        Set<String> coordsSet = govDataApiService.getOpenApiCoords();
        List<Map<String, String>> structuredAddressList = naverApiService.getAddress(coordsSet);
        dataInsertService.insertData(structuredAddressList);
    }

    @Override
    protected void insertFileApiData() throws JsonProcessingException {
        List<String> fileDataUrlList = govDataApiService.getFileDataApiUrls();
        Set<String> coordsSet = govDataApiService.getFileDataApiCoords(fileDataUrlList);
        List<Map<String, String>> structuredAddressList = naverApiService.getAddress(coordsSet);
        dataInsertService.insertData(structuredAddressList);
    }

    @Override
    protected void insertUnstructuredData() throws JsonProcessingException {
        Set<String> coordsSet = unstructedDataService.getUnstructuredDataCoords();
        List<Map<String, String>> structuredAddressList = naverApiService.getAddress(coordsSet);
        dataInsertService.insertData(structuredAddressList);
    }
}
