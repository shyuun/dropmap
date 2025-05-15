package com.dropmap.www.domain.district;

import com.dropmap.www.domain.BaseUserEntity;
import com.dropmap.www.domain.geolocation.GeolocationId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@EqualsAndHashCode(of = "id")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="DISTRICT_INFO")
public class DistrictInfo extends BaseUserEntity {

    @EmbeddedId
    private DistrictInfoId id;

    @Comment("위도")
    @Column(name="LAT")
    private Double Lat;

    @Comment("경도")
    @Column(name="LOT")
    private Double Lot;

    @Comment("경계")
    @Column(name = "BOUNDARY", columnDefinition = "MEDIUMTEXT")
    private String boundary;

    @Comment("깊이")
    @Column(name = "DEPTH")
    private int depth;
}
