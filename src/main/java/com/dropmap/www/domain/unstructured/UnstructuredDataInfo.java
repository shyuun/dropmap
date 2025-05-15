package com.dropmap.www.domain.unstructured;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="UNSTRUCTURED_DATA_INFO")
public class UnstructuredDataInfo extends BaseUserEntity {

    @Id
    @Comment("데이터순번")
    @Column(name="DATA_IDX")
    private int id;

    @Comment("위도")
    @Column(name="LAT")
    private Double Lat;

    @Comment("경도")
    @Column(name="LOT")
    private Double Lot;

    @Comment("주소")
    @Column(name="ADDRESS")
    private String address;
}
