package com.dropmap.www.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeolocationSave {
    private String lat;
    private String lot;
    private String roadAddress;
    private String lotAddress;
    private String regionCode;
    private String legalDongCode;
    private String admDongCode;

    @Builder
    public GeolocationSave(String lat, String lot, String roadAddress, String lotAddress, String regionCode, String legalDongCode, String admDongCode){
        this.lat = lat;
        this.lot = lot;
        this.roadAddress = roadAddress;
        this.lotAddress = lotAddress;
        this.regionCode = regionCode;
        this.legalDongCode = legalDongCode;
        this.admDongCode = admDongCode;
    }


}
