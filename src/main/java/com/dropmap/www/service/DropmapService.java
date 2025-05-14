package com.dropmap.www.service;

import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.dto.DistrictInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DropmapService {

    private final GeolocationInfoRepository geolocationInfoRepository;

    public List<DistrictInfoResponse> getDistrictInfo(int zoomLevel) {
        List<DistrictInfoResponse> districtInfoList = new ArrayList<>();

        if(zoomLevel <= 10){//시
            districtInfoList = geolocationInfoRepository.findCityCounts();
        } else if (zoomLevel == 11 || zoomLevel == 12){//구
            districtInfoList = geolocationInfoRepository.findRegionCounts();
        } else if (zoomLevel == 13){//법정동
            districtInfoList = geolocationInfoRepository.findLegalDongCounts();
        }
        
        return districtInfoList;
    }
}
