package com.dropmap.www.service;

import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.dto.DistrictInfoResponse;
import com.dropmap.www.dto.GeolocationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DropmapService {

    private final GeolocationInfoRepository geolocationInfoRepository;

    public List<DistrictInfoResponse> getDistrictInfo(int zoomLevel, double lat1, double lat2, double lot1, double lot2) {
        List<DistrictInfoResponse> districtInfoList = new ArrayList<>();

        if(zoomLevel <= 10){//시
            districtInfoList = geolocationInfoRepository.findCityCounts();
        } else if (zoomLevel == 11 || zoomLevel == 12 || zoomLevel == 13){//구
            List<Object[]> result = geolocationInfoRepository.findRegionCounts(lat1,lat2,lot1,lot2);
            districtInfoList = parseToDto(result);
        } else if (zoomLevel == 14){//법정동
            List<Object[]> result = geolocationInfoRepository.findLegalDongCounts(lat1,lat2,lot1,lot2);
            districtInfoList = parseToDto(result);
        }
        
        return districtInfoList;
    }

    public List<GeolocationInfoResponse> getMarkerInfo(double lat1, double lat2, double lot1, double lot2) {
        return geolocationInfoRepository.findByIdLatBetweenAndIdLotBetween(lat1,lat2,lot1,lot2).stream()
                .map(GeolocationInfoResponse::new)
                .collect(Collectors.toList());
    }

    private List<DistrictInfoResponse> parseToDto(List<Object[]> rawList) {
        return rawList.stream()
                .map(row -> new DistrictInfoResponse(
                        ((Number) row[0]).longValue(),        // COUNT(*)
                        (String) row[1],                      // pName
                        (String) row[2],                      // name
                        ((Number) row[3]).doubleValue(),      // lat
                        ((Number) row[4]).doubleValue(),      // lot
                        (String) row[5]                       // boundary
                ))
                .collect(Collectors.toList());
    }
}
