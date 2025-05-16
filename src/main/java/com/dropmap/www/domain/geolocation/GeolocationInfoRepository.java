package com.dropmap.www.domain.geolocation;

import com.dropmap.www.dto.DistrictInfoResponse;
import com.dropmap.www.dto.GeolocationInfoResponse;
import com.dropmap.www.dto.GeolocationSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GeolocationInfoRepository extends JpaRepository<GeolocationInfo, GeolocationId> {

    @Query("SELECT new com.dropmap.www.dto.DistrictInfoResponse( " +
            "COUNT(g), " +
            "(SELECT c.id.pDistrictName FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.id.districtName FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.Lat FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.Lot FROM DistrictInfo c WHERE c.depth = 0), " +
            "(SELECT c.boundary FROM DistrictInfo c WHERE c.depth = 0) " +
            ")"+
            "FROM GeolocationInfo g ")
    List<DistrictInfoResponse> findCityCounts();

            @Query(value = """
                SELECT 
                    A1.cnt AS count,
                    NULL AS pName,
                    A1.region_name AS name,
                    A1.lat AS lat,
                    A1.lot AS lot,
                    A2.BOUNDARY AS boundary
                FROM (
                    SELECT 
                        COUNT(*) AS cnt,
                        gi.REGION_NAME AS region_name,
                        di.LAT AS lat,
                        di.LOT AS lot
                    FROM GEOLOCATION_INFO gi
                    JOIN DISTRICT_INFO di ON di.DISTRICT_NAME = gi.REGION_NAME
                    WHERE di.LAT BETWEEN :lat1 AND :lat2
                      AND di.LOT BETWEEN :lot1 AND :lot2
                    GROUP BY gi.REGION_NAME, di.LAT, di.LOT
                ) A1
                JOIN DISTRICT_INFO A2 
                  ON A1.region_name = A2.DISTRICT_NAME
                 AND A1.lat = A2.LAT
                 AND A1.lot = A2.LOT
            """, nativeQuery = true)
            List<Object[]> findRegionCounts(@Param("lat1") double lat1, @Param("lat2") double lat2, @Param("lot1") double lot1, @Param("lot2") double lot2);

            @Query(value = """
                SELECT 
                    A1.cnt AS count,
                    A1.region_name AS pName,
                    A1.legal_dong_name AS name,
                    A1.lat AS lat,
                    A1.lot AS lot,
                    A2.BOUNDARY AS boundary
                FROM (
                    SELECT 
                        COUNT(*) AS cnt,
                        gi.REGION_NAME AS region_name,
                        gi.LEGAL_DONG_NAME AS legal_dong_name,
                        di.LAT AS lat,
                        di.LOT AS lot
                    FROM GEOLOCATION_INFO gi
                    JOIN DISTRICT_INFO di 
                    ON di.P_DISTRICT_NAME = gi.REGION_NAME
                    AND di.DISTRICT_NAME = gi.LEGAL_DONG_NAME
                    WHERE di.LAT BETWEEN :lat1 AND :lat2
                      AND di.LOT BETWEEN :lot1 AND :lot2
                    GROUP BY gi.REGION_NAME, di.LAT, di.LOT
                ) A1
                JOIN DISTRICT_INFO A2 
                  ON A1.region_name = A2.P_DISTRICT_NAME
                 AND A1.legal_dong_name = A2.DISTRICT_NAME
                 AND A1.lat = A2.LAT
                 AND A1.lot = A2.LOT
            """, nativeQuery = true)
            List<Object[]> findLegalDongCounts(@Param("lat1") double lat1, @Param("lat2") double lat2, @Param("lot1") double lot1, @Param("lot2") double lot2);


            List<GeolocationInfo> findByIdLatBetweenAndIdLotBetween(double lat1, double lat2, double lot1, double lot2);

}
