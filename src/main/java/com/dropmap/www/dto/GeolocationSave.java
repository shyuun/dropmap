package com.dropmap.www.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeolocationSave {
    private Double lat;
    private Double lot;
    private String roadAddress;
    private String lotAddress;
    private String regionName;
    private String legalDongName;
    private String admDongName;

    @Builder
    public GeolocationSave(Double lat, Double lot, String roadAddress, String lotAddress, String regionName, String legalDongName, String admDongName){
        this.lat = lat;
        this.lot = lot;
        this.roadAddress = roadAddress;
        this.lotAddress = lotAddress;
        this.regionName = regionName;
        this.legalDongName = legalDongName;
        this.admDongName = admDongName;
    }


}
