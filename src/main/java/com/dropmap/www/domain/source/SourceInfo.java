package com.dropmap.www.domain.source;

import com.dropmap.www.domain.BaseUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="SOURCE_INFO")
public class SourceInfo extends BaseUserEntity {

    @Id
    @Comment("데이터순번")
    @Column(name="DATA_IDX")
    private Long dataIdx;

    @Comment("출처타입")
    @Column(name="SOURCE_TYPE")
    private String sourceType;

    @Comment("출처타입명")
    @Column(name="SOURCE_TYPE_NAME")
    private String sourceTypeName;

    @Comment("출처데이터명")
    @Column(name="SOURCE_DATA_NAME")
    private String sourceDataName;

    @Comment("소스경로")
    @Column(name="SOURCE_PATH")
    private String sourcePath;

    @Comment("출처코드")
    @Column(name="SOURCE_CODE")
    private String sourceCode;

    @Comment("업데이트여부")
    @Column(name="UPT_YN")
    private String uptYn;
}
