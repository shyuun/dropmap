package com.dropmap.www.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseUserEntity {

    @Comment("사용여부")
    @Column(name="USE_YN", columnDefinition = "VARCHAR(1) DEFAULT 'Y'")
    private String useYn;

    @Comment("등록일자")
    @CreatedDate
    @Column(name="REG_DT", updatable = false)
    private LocalDateTime regDt;

    @Comment("등록자명")
    @CreatedBy
    @Column(name="REG_NM")
    private String regNm;

    @Comment("수정일자")
    @LastModifiedDate
    @Column(name="UPT_DT")
    private LocalDateTime uptDt;

    @Comment("수정자명")
    @LastModifiedBy
    @Column(name="UPT_NM")
    private String uptNm;

    @PrePersist
    public void prePersist() {
        if (this.useYn == null) { // 값이 없는 경우 기본값 설정
            this.useYn = "Y";
        }
    }
}
