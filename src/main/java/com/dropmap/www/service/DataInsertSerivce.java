package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfo;
import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.service.api.NaverApiService;
import com.dropmap.www.service.api.VworldApiService;
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
    private final NaverApiService naverApiService;
    private final VworldApiService vworldApiService;

    public void insertData(List<Map<String, String>> structuredAddressList) {

        String cmnNm = "",pCmnNm = "",ppCmnNm = "";
        String cmnCd ="A0001", pCmnCd = "A0001", regionCode = "";
        Set<String> regionCodeSet = new HashSet<>();
        int pNum = 0,ppNum = 0;
        int ppp = 0;

        for(Map<String,String> m : structuredAddressList){
            
            //오류데이터 NULL처리
            for (Map.Entry<String, String> entry : m.entrySet()) {
                if (entry.getValue().trim().isEmpty()) {
                    entry.setValue(null);
                }
            }
            
            String[] areaArr = {m.get("area0"),m.get("area1"),m.get("area2"),m.get("area3"),m.get("area4")};//{대한민국,서울특별시,강남구,역삼동,역삼 2동}
            
            for(int i=1; i<areaArr.length; i++){
                cmnNm=areaArr[i];
                pCmnNm=areaArr[i-1];
                ppCmnNm = m.get("area0");
                pNum = i-1;

                if(i>1) {
                    ppCmnNm=areaArr[i-2];
                    ppNum = i-2;
                }

                //키존재 확인
                int resultCnt = districtInfoRepository.existsDistrictNameAndParentName(pCmnNm,cmnNm);

                if(resultCnt < 1){
                    char prefix = (char) ('A' + (pNum));
                    if(i != 1){
                        //부모키 가져옴
                        pCmnCd = districtInfoRepository.findParentDistrictCodeByDistrictName(ppCmnNm,pCmnNm,ppNum);

                        //새키 생성
                        cmnCd = districtInfoRepository.findDistrictCodeByDistrictName(prefix,pNum);
                    }

                    if(i==2){
                        regionCode = cmnCd;
                    }

                    String address = "대한민국".equals(pCmnNm) ? cmnNm : pCmnNm + " " + cmnNm;

                    if(i == 4){
                        address = ppCmnNm + " " + cmnNm;//강남구 역삼2동
                    }

                    String lot = null, lat = null;
                    String coords = naverApiService.getCoord(address);

                    if(coords != null){
                        lot = coords.split(",")[0];
                        lat = coords.split(",")[1];
                    }
                    //TODO
                    String boundary = vworldApiService.getBoundary(coords,pNum);

                    districtInfoRepository.save(DistrictInfo.builder()
                                    .districtCode(cmnCd)
                                    .pDistrictCode(pCmnCd)
                                    .districtName(cmnNm)
                                    .pDistrictName(pCmnNm)
                                    .boundary(boundary)
                                    .Lat(lat)
                                    .Lot(lot)
                                    .depth(pNum)
                            .build());
                }
            }

            int resultCnt = geoLocationRepository.insertWithRegionCodes(m.get("latitude"),m.get("longitude"),m.get("area2"),m.get("area3"),m.get("area4"),m.get("roadAddress"),m.get("lotAddress"));
            ppp ++;

            regionCodeSet.add(regionCode);
        }

        return regionCodeSet;
    }
}
