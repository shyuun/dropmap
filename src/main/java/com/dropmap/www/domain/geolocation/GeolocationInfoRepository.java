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

    @Query("SELECT new com.dropmap.www.dto.DistrictInfoResponse( " +
            "COUNT(g), " +
            "NULL, " +
            "g.regionName, " +
            "d.Lat, " +
            "d.Lot, " +
            "d.boundary" +
            ")  " +
            "FROM GeolocationInfo g " +
            "JOIN DistrictInfo d " +
            "ON d.id.districtName = g.regionName " +
            "GROUP BY g.regionName,d.Lat,d.Lot,d.boundary")
    List<DistrictInfoResponse> findRegionCounts();

    @Query("SELECT new com.dropmap.www.dto.DistrictInfoResponse( " +
            "COUNT(g), " +
            "g.regionName, " +
            "g.legalDongName, " +
            "d.Lat, " +
            "d.Lot, " +
            "d.boundary" +
            ")  " +
            "FROM GeolocationInfo g " +
            "JOIN DistrictInfo d " +
            "ON d.id.pDistrictName = g.regionName " +
            "AND d.id.districtName = g.legalDongName " +
            "GROUP BY g.regionName,g.legalDongName,d.Lat,d.Lot,d.boundary")
    List<DistrictInfoResponse> findLegalDongCounts();
}
