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
    private String Lat;

    @Comment("경도")
    @Column(name="LOT")
    private String Lot;
    
    public GeolocationId() {}

    @Builder
    public GeolocationId(String lat, String lot) {
        this.Lat = lat;
        this.Lot = lot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeolocationId GeolocationId = (GeolocationId) o;
        return Objects.equals(Lat, GeolocationId.Lat) && Objects.equals(Lot, GeolocationId.Lot);
    }

    @Override
    public int hashCode(){
        return Objects.hash(Lat, Lot);
    }
}
