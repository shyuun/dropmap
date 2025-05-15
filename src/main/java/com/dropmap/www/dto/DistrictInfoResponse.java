package com.dropmap.www.dto;

import com.dropmap.www.domain.BaseUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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
