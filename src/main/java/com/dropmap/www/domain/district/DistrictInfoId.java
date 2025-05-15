package com.dropmap.www.domain.district;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class DistrictInfoId implements Serializable {

    @Comment("상위구역명")
    @Column(name = "P_DISTRICT_NAME")
    private String pDistrictName;

    @Comment("행정구역명")
    @Column(name = "DISTRICT_NAME")
    private String districtName;

    public DistrictInfoId() {}

    @Builder
    public DistrictInfoId(String pDistrictName, String districtName) {
        this.pDistrictName = pDistrictName;
        this.districtName = districtName;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        DistrictInfoId districtInfoId = (DistrictInfoId) o;
        return Objects.equals(pDistrictName, districtInfoId.pDistrictName) && Objects.equals(districtName, districtInfoId.districtName);
    }

    @Override
    public int hashCode(){return Objects.hash(pDistrictName, districtName); }
}
