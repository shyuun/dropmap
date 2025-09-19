package com.dropmap.www.service;

import com.dropmap.www.component.DistrictExistCache;
import com.dropmap.www.domain.district.DistrictInfo;
import com.dropmap.www.domain.district.DistrictInfoId;
import com.dropmap.www.domain.district.DistrictInfoRepository;
import com.dropmap.www.domain.geolocation.GeolocationInfoRepository;
import com.dropmap.www.domain.source.SourceInfoRepository;
import com.dropmap.www.dto.GeolocationSave;
import com.dropmap.www.service.api.NaverApiService;
import com.dropmap.www.service.api.VworldApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class DataInsertSerivce {

    private final DataSource dataSource;
    private final DistrictInfoRepository districtInfoRepository;
    private final GeolocationInfoRepository geolocationInfoRepository;
    private final NaverApiService naverApiService;
    private final VworldApiService vworldApiService;
    private final SourceInfoRepository sourceInfoRepository;
    private final DistrictExistCache districtExistCache;

    @Transactional
    public void insertData(List<Map<String, String>> addressList) throws JsonProcessingException, SQLException {

        String regionName = "";
        Set<String> regionNameSet = new HashSet<>();
        LinkedHashSet<DistrictInfo> districtInfoSet = new LinkedHashSet<>();
        LinkedHashSet<GeolocationSave> geolocationSaveSet = new LinkedHashSet<>();
        Map<String, CompletableFuture<String>> futureBoundaryMap = new HashMap<>();
        Map<String, DistrictInfo> districtInfoMap = new HashMap<>();

        for(Map<String,String> m : addressList){
            regionNameSet.add(m.get("area2"));

            //오류데이터 NULL처리
            for (Map.Entry<String, String> entry : m.entrySet()) {
                if (entry.getValue().trim().isEmpty()) {
                    entry.setValue(null);
                }
            }
            
            //{m.get("area0"),m.get("area1"),m.get("area2"),m.get("area3"),m.get("area4")};//{대한민국,서울특별시,강남구,역삼동,역삼 2동}

            for(int i=0; i<3; i++){
                String pDistrictName = m.get("area"+i);
                String districtName = m.get("area"+(i+1));
                String key = pDistrictName + "_" + districtName;

                if(districtInfoMap.containsKey(key)) continue;//Map에 존재하지 않을때만 진행
                if (districtExistCache.isExist(key)) continue;//캐시변수 중복 체크

                //Set에도 존재하지않고 db에도 존재하지 않으면 DistrictInfo 저장을 위한 DTO 생성
                String address = switch (i) {
                    case 0 -> m.get("area1");//서울특별시
                    case 3 -> m.get("area2") + " " + m.get("area4");//강남구 역삼2동
                    default -> pDistrictName + " " + districtName;
                };

                String lot = null, lat = null;

                String coords = naverApiService.getCoord(address);//주소 -> 위경도 데이터

                if(coords != null){
                    lot = coords.split(",")[0];
                    lat = coords.split(",")[1];

                    // 비동기로 boundary 호출
                    futureBoundaryMap.put(key, vworldApiService.getBoundaryAsync(coords, i));
                }

                //구역정보 set에 추가
                DistrictInfo info = DistrictInfo.builder()
                        .id(DistrictInfoId.builder()
                                .pDistrictName(pDistrictName)
                                .districtName(districtName)
                                .build())
                        .Lat(Double.parseDouble(lat))
                        .Lot(Double.parseDouble(lot))
                        .depth(i)
                        .build();

                districtInfoMap.put(key, info);
                districtExistCache.put(key, true);
            }

            //의류수거함 정보 set에 추가
            geolocationSaveSet.add(GeolocationSave.builder()
                    .lat(Double.parseDouble(m.get("lat")))
                    .lot(Double.parseDouble(m.get("lot")))
                    .roadAddress(m.get("roadAddress"))
                    .lotAddress(m.get("lotAddress"))
                    .regionName(m.get("area2"))
                    .legalDongName(m.get("area3"))
                    .admDongName(m.get("area4"))
                    .build());
        }

        // 비동기 호출 결과 대기 및 반영
        CompletableFuture.allOf(futureBoundaryMap.values().toArray(new CompletableFuture[0])).join();
        for (Map.Entry<String, CompletableFuture<String>> entry : futureBoundaryMap.entrySet()) {
            String key = entry.getKey();
            String boundary = entry.getValue().join();
            districtInfoMap.get(key).setBoundary(boundary);
        }

        districtInfoSet.addAll(districtInfoMap.values());

        //행정구역정보 BULK INSERT
        batchInsertDistrictInfo(districtInfoSet);

        //위경도 정보 BULK INSERT
        batchInsertGeolocation(geolocationSaveSet);

        //출처정보 테이블에 업데이트여부 'Y'처리
        regionNameSet.forEach(sourceInfoRepository::updateUptYnBySourceDataName);
    }

    public void batchInsertGeolocation(LinkedHashSet<GeolocationSave> geolocationList) throws SQLException {
        String sql = """
        INSERT IGNORE INTO GEOLOCATION_INFO 
        (LAT, LOT, REGION_NAME, LEGAL_DONG_NAME, ADM_DONG_NAME, ROAD_ADDRESS, LOT_ADDRESS, USE_YN, REG_DT, REG_NM, UPT_DT, UPT_NM)
        VALUES (?, ?, ?, ?, ?, ?, ?, 'Y', NOW(), 'SYSTEM', NOW(), 'SYSTEM')
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // 트랜잭션 시작

            int batchSize = 1000;
            int count = 0;

            for (GeolocationSave item : geolocationList) {
                pstmt.setDouble(1, item.getLat());
                pstmt.setDouble(2, item.getLot());
                pstmt.setString(3, item.getRegionName());
                pstmt.setString(4, item.getLegalDongName());
                pstmt.setString(5, item.getAdmDongName());
                pstmt.setString(6, item.getRoadAddress());
                pstmt.setString(7, item.getLotAddress());

                pstmt.addBatch();
                count++;

                if (count % batchSize == 0) {
                    pstmt.executeBatch();
                }
            }

            pstmt.executeBatch(); // 남은 데이터 실행
            conn.commit();        // 전체 커밋
        } catch (SQLException e) {
            throw new RuntimeException("배치 인서트 실패", e);
        }
    }

    public void batchInsertDistrictInfo(Set<DistrictInfo> districtInfoSet) throws SQLException {
        String sql = """
        INSERT IGNORE INTO DISTRICT_INFO
        (P_DISTRICT_NAME, DISTRICT_NAME, LAT, LOT, BOUNDARY, DEPTH, USE_YN, REG_DT, REG_NM, UPT_DT, UPT_NM)
        VALUES (?, ?, ?, ?, ?, ?, 'Y', NOW(), 'SYSTEM', NOW(), 'SYSTEM')
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            int batchSize = 1000;
            int count = 0;

            for (DistrictInfo info : districtInfoSet) {
                pstmt.setString(1, info.getId().getPDistrictName());
                pstmt.setString(2, info.getId().getDistrictName());
                pstmt.setDouble(3, info.getLat());
                pstmt.setDouble(4, info.getLot());
                pstmt.setString(5, info.getBoundary());
                pstmt.setInt(6, info.getDepth());

                pstmt.addBatch();
                count++;

                if (count % batchSize == 0) {
                    pstmt.executeBatch();
                }
            }

            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("DistrictInfo 배치 인서트 실패", e);
        }
    }
}
