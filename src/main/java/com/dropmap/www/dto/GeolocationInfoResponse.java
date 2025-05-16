package com.dropmap.www.dto;

import com.dropmap.www.domain.BaseUserEntity;
import com.dropmap.www.domain.geolocation.GeolocationInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GeolocationInfoResponse extends BaseUserEntity {
    private final Double Lat;
    private final Double Lot;
    private final String roadAddress;
    private final String lotAddress;

    public GeolocationInfoResponse(GeolocationInfo entity) {
        this.Lat = entity.getId().getLat();
        this.Lot = entity.getId().getLot();
        this.roadAddress = entity.getRoadAddress();
        this.lotAddress = entity.getLotAddress();
    }
}
