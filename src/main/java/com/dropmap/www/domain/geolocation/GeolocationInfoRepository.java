package com.dropmap.www.domain.geolocation;

import com.dropmap.www.dto.DistrictInfoResponse;
import com.dropmap.www.dto.GeolocationSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GeolocationInfoRepository extends JpaRepository<GeolocationInfo, GeolocationId> {

    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO GEOLOCATION_INFO 
                (LAT, LOT, REGION_CODE, LEGAL_DONG_CODE, ADM_DONG_CODE, ROAD_ADDRESS, LOT_ADDRESS, USE_YN, REG_DT, REG_NM, UPT_DT, UPT_NM)
                SELECT
                :#{#geo.lat},
                :#{#geo.lot},
                REGION.DISTRICT_CODE,
                LEGAL.DISTRICT_CODE,
                ADMIN.DISTRICT_CODE,
                :#{#geo.roadAddress},
                :#{#geo.lotAddress},
                'Y',
                NOW(),
                'SYSTEM',
                NOW(),
                'SYSTEM'
                FROM DISTRICT_INFO REGION
                JOIN DISTRICT_INFO LEGAL ON LEGAL.P_DISTRICT_CODE = REGION.DISTRICT_CODE AND LEGAL.DEPTH = 2
                JOIN DISTRICT_INFO ADMIN ON ADMIN.P_DISTRICT_CODE = LEGAL.DISTRICT_CODE AND ADMIN.DEPTH = 3
                WHERE REGION.DEPTH = 1
                AND REGION.DISTRICT_NAME = :#{#geo.regionName}
                AND LEGAL.DISTRICT_NAME = :#{#geo.legalDongName}
                AND ADMIN.DISTRICT_NAME = :#{#geo.admDongName}
            """,nativeQuery = true)
    int insertGeolocation(@Param("geo") GeolocationSave geolocationSave);
    
    @Query("SELECT new com.dropmap.www.dto.DistrictInfoResponse( " +
            "COUNT(g), " +
            "(SELECT c.districtCode FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.districtName FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.Lat FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.Lot FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.boundary FROM DistrictInfo c WHERE c.depth = 0) " +
            ")"+
            "FROM GeolocationInfo g ")
    List<DistrictInfoResponse> findCityCounts();

    @Query("SELECT new com.dropmap.www.dto.DistrictInfoResponse( " +
            "COUNT(g), " +
            "g.regionCode, " +
            "d.districtName, " +
            "d.Lat, " +
            "d.Lot, " +
            "d.boundary" +
            ")  " +
            "FROM GeolocationInfo g " +
            "JOIN DistrictInfo d " +
            "ON d.districtCode = g.regionCode " +
            "GROUP BY g.regionCode,d.Lat,d.Lot,d.districtName,d.boundary")
    List<DistrictInfoResponse> findRegionCounts();

    @Query("SELECT new com.dropmap.www.dto.DistrictInfoResponse( " +
            "COUNT(g), " +
            "g.legalDongCode, " +
            "d.districtName," +
            "d.Lat, " +
            "d.Lot, " +
            "d.boundary" +
            ")  " +
            "FROM GeolocationInfo g " +
            "JOIN DistrictInfo d " +
            "ON d.districtCode = g.legalDongCode " +
            "GROUP BY g.regionCode,g.legalDongCode,d.Lat,d.Lot,d.districtName,d.boundary")
    List<DistrictInfoResponse> findLegalDongCounts();
}
