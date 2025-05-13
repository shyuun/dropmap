package com.dropmap.www.domain.district;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictInfoRepository extends JpaRepository<DistrictInfo, String> {


    @Query(value="SELECT EXISTS (SELECT 1 FROM DISTRICT_INFO c WHERE c.DISTRICT_NAME = :districtName AND c.P_DISTRICT_NAME = :pDistrictName)", nativeQuery = true)
    int existsDistrictNameAndParentName(@Param("pDistrictName") String pDistrictName, @Param("districtName") String districtName);

    @Query("SELECT DISTINCT c.districtCode FROM DistrictInfo c WHERE c.pDistrictName = :pDistrictName AND c.districtName = :districtName AND c.depth = :depth")
    String findParentDistrictCodeByDistrictName(@Param("pDistrictName") String pDistrictName, @Param("districtName") String districtName, @Param("depth") int depth);

    @Query(value = "SELECT IFNULL(CONCAT(:prefix,LPAD(CAST(SUBSTRING(MAX(c.DISTRICT_CODE), 2) AS UNSIGNED)+1,4,'0')),concat(:prefix,'0001'))"+
            "FROM DISTRICT_INFO c "+
            "WHERE c.DEPTH=:depth", nativeQuery = true)
    String findDistrictCodeByDistrictName(@Param("prefix") char prefix, @Param("depth") int depth);
}
