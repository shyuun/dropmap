package com.dropmap.www.domain.source;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SourceInfoRepository extends JpaRepository<SourceInfo, Long> {

    @Modifying
    @Transactional
    @Query("""
    UPDATE SourceInfo s
    SET s.uptYn = 'Y'
    WHERE s.useYn = 'Y'
    AND s.sourceDataName = :sourceDataName
""")
    void updateUptYnBySourceDataName(@Param("sourceDataName") String regionNm);

    List<SourceInfo> findBySourceTypeInAndUptYnAndSourceCodeIsNotNull(List<String> sourceTypes, String uptYn);

    @Modifying
    @Query("UPDATE SourceInfo s SET s.uptYn = 'N'")
    void resetUptYnAll();
}
