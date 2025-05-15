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

    @Comment("자치구명")
    @Column(name="REGION_NAME")
    private String regionName;

    @Comment("법정동명")
    @Column(name="LEGAL_DONG_NAME")
    private String legalDongName;

    @Comment("행정동명")
    @Column(name="ADM_DONG_NAME")
    private String admDongName;

    @Comment("도로명주소")
    @Column(name="ROAD_ADDRESS")
    private String roadAddress;

    @Comment("지번")
    @Column(name="LOT_ADDRESS")
    private String lotAddress;
}
