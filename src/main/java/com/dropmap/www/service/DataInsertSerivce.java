package com.dropmap.www.service;

import com.dropmap.www.domain.district.DistrictInfo;
import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.dto.GeolocationSave;
import com.dropmap.www.service.api.NaverApiService;
import com.dropmap.www.service.api.VworldApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class DataInsertSerivce {

    private final DistrictInfoRepository districtInfoRepository;
    private final GeolocationInfoRepository geolocationInfoRepository;
    private final NaverApiService naverApiService;
    private final VworldApiService vworldApiService;
    private final JdbcTemplate jdbcTemplate;

    public void insertData(List<Map<String, String>> addressList) throws JsonProcessingException {

        String dNm = "",pDNm = "",ppDNm = "";
        String dCd ="A0001", pDCd = "A0001", regionCode = "";
        Set<String> regionCodeSet = new HashSet<>();
        int pNum = 0,ppNum = 0;
        int ppp = 0;
        List<GeolocationSave> geolocationSaveList = new ArrayList<>();

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

                //키존재 확인
                int resultCnt = districtInfoRepository.existsDistrictNameAndParentName(pDNm,dNm);

                if(resultCnt < 1){
                    char prefix = (char) ('A' + (pNum));
                    if(i == 1){
                        pDCd = "A0001";//서울특별시
                        dCd = "A0001";//서울특별시
                    } else {
                        //부모키 가져옴
                        pDCd = districtInfoRepository.findParentDistrictCodeByDistrictName(ppDNm,pDNm,ppNum);

                        //새키 생성
                        dCd = districtInfoRepository.findDistrictCodeByDistrictName(prefix,pNum);
                    }

                    if(i==2){
                        regionCode = dCd;
                    }

                    String address = "대한민국".equals(pDNm) ? dNm : pDNm + " " + dNm;

                    if(i == 4){
                        address = ppDNm + " " + dNm;//강남구 역삼2동
                    }

                    String lot = null, lat = null;
                    String coords = naverApiService.getCoord(address);

                    if(coords != null){
                        lot = coords.split(",")[0];
                        lat = coords.split(",")[1];
                    }

                    String boundary = vworldApiService.getBoundary(coords,pNum);

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

            ppp ++;

//            geolocationInfoRepository.insertGeolocation(GeolocationSave.builder()
//                    .lat(m.get("lat"))
//                    .lot(m.get("lot"))
//                    .roadAddress(m.get("roadAddress"))
//                    .lotAddress(m.get("lotAddress"))
//                    .regionName(m.get("area2"))
//                    .legalDongName(m.get("area3"))
//                    .admDongName(m.get("area4"))
//                    .build());

            List<Object[]> districtCodes = districtInfoRepository.findDistrictCodeByDistrictName(m.get("area2"),m.get("area3"),m.get("area4"));


            //BULK INSERT를 위한 데이터 LIST 추가
            geolocationSaveList.add(GeolocationSave.builder()
                    .lat(m.get("lat"))
                    .lot(m.get("lot"))
                    .roadAddress(m.get("roadAddress"))
                    .lotAddress(m.get("lotAddress"))
                    .regionCode((String) districtCodes.get(0)[0])
                    .legalDongCode((String) districtCodes.get(0)[1])
                    .admDongCode((String) districtCodes.get(0)[2])
                    .build());
        }
        //FOR문 종료 후 BULKINSERT
        bulkInsertGeolocationInfo(geolocationSaveList);
    }

    @Transactional
    public void bulkInsertGeolocationInfo(List<GeolocationSave> geolocationSaveList) throws JsonProcessingException {
        String sql = """
                INSERT INTO GEOLOCATION_INFO 
                (LAT, LOT, REGION_CODE, LEGAL_DONG_CODE, ADM_DONG_CODE, ROAD_ADDRESS, LOT_ADDRESS, USE_YN, REG_DT, REG_NM, UPT_DT, UPT_NM)
                VALUES(?,?,?,?,?,?,?,'Y',NOW(),'SYSTEM',NOW(),'SYSTEM')
                """;
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                GeolocationSave geolocationSave = geolocationSaveList.get(i);
                ps.setString(1, geolocationSave.getLat());
                ps.setString(2, geolocationSave.getLot());
                ps.setString(3, geolocationSave.getRegionCode());
                ps.setString(4, geolocationSave.getLegalDongCode());
                ps.setString(5, geolocationSave.getAdmDongCode());
                ps.setString(6, geolocationSave.getRoadAddress());
                ps.setString(7, geolocationSave.getLotAddress());
            }

            @Override
            public int getBatchSize() {
                return geolocationSaveList.size();
            }
        });
    }
}
