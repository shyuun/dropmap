package com.dropmap.www.domain.district;

import com.dropmap.www.domain.BaseUserEntity;
import jakarta.persistence.*;
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
@Table(name="DISTRICT_INFO")
public class DistrictInfo extends BaseUserEntity {

    @Id
    @Comment("구역코드")
    @Column(name = "DISTRICT_CODE")
    private String districtCode;

    @Comment("행정구역명")
    @Column(name = "DISTRICT_NAME")
    private String districtName;

    @Comment("상위구역코드")
    @Column(name = "P_DISTRICT_CODE")
    private String pDistrictCode;

    @Comment("상위구역명")
    @Column(name = "P_DISTRICT_NAME")
    private String pDistrictName;

    @Comment("위도")
    @Column(name="LAT")
    private String Lat;

    @Comment("경도")
    @Column(name="LOT")
    private String Lot;

    @Comment("경계")
    @Column(name = "BOUNDARY", columnDefinition = "MEDIUMTEXT")
    private String boundary;

    @Comment("깊이")
    @Column(name = "DEPTH")
    private int depth;
}
