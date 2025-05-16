package com.dropmap.www.dto;

import com.dropmap.www.domain.BaseUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DistrictInfoResponse extends BaseUserEntity {
    private final Long count;
    private final String pName;
    private final String name;
    private final Double Lat;
    private final Double Lot;
    private final String boundary;
}
