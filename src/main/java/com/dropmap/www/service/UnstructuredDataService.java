package com.dropmap.www.service;

import com.dropmap.www.domain.unstructured.UnstructuredDataInfo;
import com.dropmap.www.domain.unstructured.UnstructuredDataInfoRepository;
import com.dropmap.www.service.api.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UnstructuredDataService {
    private final NaverApiService naverApiService;
    private final UnstructuredDataInfoRepository unstructuredDataInfoRepository;

    public Set<String> getUnstructuredDataCoords() {
        Set<String> coordsSet = new HashSet<>();
        List<UnstructuredDataInfo> unstructuredDataList =  unstructuredDataInfoRepository.findAll();
        if(!unstructuredDataList.isEmpty()){
            unstructuredDataList.forEach(unstructuredData -> {
                if(unstructuredData.getAddress() == null){
                    coordsSet.add(unstructuredData.getLot() + ","+ unstructuredData.getLat());
                } else {
                    String coordStr = naverApiService.getCoord(unstructuredData.getAddress());
                    if(coordStr != null){
                        coordsSet.add(coordStr);
                    }
                }
            });
        }

        return coordsSet;
    }
}
