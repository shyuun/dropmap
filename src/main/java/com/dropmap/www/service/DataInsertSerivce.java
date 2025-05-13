package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfo;
import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.domain.source.SourceInfoRepository;
import com.dropmap.www.dto.GeolocationSave;
import com.dropmap.www.service.api.NaverApiService;
import com.dropmap.www.service.api.VworldApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class DataInsertSerivce {

    private final DistrictInfoRepository districtInfoRepository;
    private final GeolocationInfoRepository geolocationInfoRepository;
    private final NaverApiService naverApiService;
    private final VworldApiService vworldApiService;
    private final SourceInfoRepository sourceInfoRepository;

    public void insertData(List<Map<String, String>> addressList) throws JsonProcessingException {

        int pNum = 0,ppNum = 0;
        String dNm = "",pDNm = "",ppDNm = "",dCd ="A0001", pDCd = "A0001", regionName = "";
        Set<String> regionNameSet = new HashSet<>();

        for(Map<String,String> m : addressList){
            
            //오류데이터 NULL처리
            for (Map.Entry<String, String> entry : m.entrySet()) {
                if (entry.getValue().trim().isEmpty()) {
                    entry.setValue(null);
                }
            }
            
            String[] areaArr = {m.get("area0"),m.get("area1"),m.get("area2"),m.get("area3"),m.get("area4")};//{대한민국,서울특별시,강남구,역삼동,역삼 2동}
            
            for(int i=1; i<areaArr.length; i++){
                dNm=areaArr[i];
                pDNm=areaArr[i-1];
                ppDNm = m.get("area0");
                pNum = i-1;

                if(i>1) {
                    ppDNm=areaArr[i-2];
                    ppNum = i-2;
                }

                int resultCnt = districtInfoRepository.existsDistrictNameAndParentName(pDNm,dNm);//키존재 확인

                if(resultCnt < 1){
                    char prefix = (char) ('A' + (pNum));
                    if(i == 1){
                        pDCd = "A0001";//서울특별시
                        dCd = "A0001";//서울특별시
                    } else {
                        pDCd = districtInfoRepository.findParentDistrictCodeByDistrictName(ppDNm,pDNm,ppNum);//부모키 가져옴
                        dCd = districtInfoRepository.findDistrictCodeByDistrictName(prefix,pNum);//새키 생성
                    }

                    if(i==2){
                        regionName = dNm;
                    }

                    String address = "대한민국".equals(pDNm) ? dNm : pDNm + " " + dNm;

                    if(i == 4){
                        address = ppDNm + " " + dNm;//강남구 역삼2동
                    }

                    String lot = null, lat = null;
                    String coords = naverApiService.getCoord(address);//주소 -> 위경도 데이터

                    if(coords != null){
                        lot = coords.split(",")[0];
                        lat = coords.split(",")[1];
                    }

                    String boundary = vworldApiService.getBoundary(coords,pNum);//경계 데이터

                    //시구동 행정구역정보 및 경계정보 추가
                    districtInfoRepository.save(DistrictInfo.builder()
                                    .districtCode(dCd)
                                    .pDistrictCode(pDCd)
                                    .districtName(dNm)
                                    .pDistrictName(pDNm)
                                    .boundary(boundary)
                                    .Lat(lat)
                                    .Lot(lot)
                                    .depth(pNum)
                            .build());
                }
            }

            //위경도 정보 insert
            geolocationInfoRepository.insertGeolocation(GeolocationSave.builder()
                    .lat(m.get("lat"))
                    .lot(m.get("lot"))
                    .roadAddress(m.get("roadAddress"))
                    .lotAddress(m.get("lotAddress"))
                    .regionName(m.get("area2"))
                    .legalDongName(m.get("area3"))
                    .admDongName(m.get("area4"))
                    .build());

            regionNameSet.add(regionName);
        }

        //출처정보 테이블에 업데이트여부 'Y'처리
        regionNameSet.forEach(sourceInfoRepository::updateUptYnBySourceDataName);
    }
}
