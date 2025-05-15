package com.dropmap.www.domain.district;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DistrictInfoRepository extends JpaRepository<DistrictInfo, DistrictInfoId> {


    @Query(value="SELECT CASE WHEN EXISTS (SELECT 1 FROM DistrictInfo c WHERE c.id.pDistrictName = :pDistrictName AND c.id.districtName = :districtName) THEN TRUE ELSE FALSE END")
    boolean existsDistrictNameAndParentName(@Param("pDistrictName") String pDistrictName, @Param("districtName") String districtName);

//    @Query("SELECT DISTINCT c.districtCode FROM DistrictInfo c WHERE c.pDistrictName = :pDistrictName AND c.districtName = :districtName AND c.depth = :depth")
//    String findParentDistrictCodeByDistrictName(@Param("pDistrictName") String pDistrictName, @Param("districtName") String districtName, @Param("depth") int depth);
//
//    @Query(value = "SELECT IFNULL(CONCAT(:prefix,LPAD(CAST(SUBSTRING(MAX(c.DISTRICT_CODE), 2) AS UNSIGNED)+1,4,'0')),concat(:prefix,'0001'))"+
//            "FROM DISTRICT_INFO c "+
//            "WHERE c.DEPTH=:depth", nativeQuery = true)
//    String findDistrictCodeByDistrictName(@Param("prefix") char prefix, @Param("depth") int depth);
}
