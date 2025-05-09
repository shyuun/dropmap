package com.dropmap.www.domain.geolocation;

import com.dropmap.www.domain.BaseUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table(name="GEOLOCATION_INFO")
public class GeolocationInfo extends BaseUserEntity {

    @EmbeddedId
    private GeolocationId id;

    @Comment("구코드")
    @Column(name="REGION_CODE")
    private String regionCode;

    @Comment("법정동코드")
    @Column(name="LEGAL_DONG_CODE")
    private String legalDongCode;

    @Comment("행정동코드")
    @Column(name="ADM_DONG_CODE")
    private String admDongCode;

    @Comment("도로명주소")
    @Column(name="ROAD_ADDRESS")
    private String roadAddress;

    @Comment("지번")
    @Column(name="LOT_ADDRESS")
    private String lotAddress;
}
