package com.dropmap.www.domain.geolocation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
public class GeolocationId implements Serializable {
    
    @Comment("위도")
    @Column(name="LAT")
    private Double lat;

    @Comment("경도")
    @Column(name="LOT")
    private Double lot;
    
    public GeolocationId() {}

    @Builder
    public GeolocationId(Double lat, Double lot) {
        this.lat = lat;
        this.lot = lot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeolocationId GeolocationId = (GeolocationId) o;
        return Objects.equals(lat, GeolocationId.lat) && Objects.equals(lot, GeolocationId.lot);
    }

    @Override
    public int hashCode(){
        return Objects.hash(lat, lot);
    }
}
