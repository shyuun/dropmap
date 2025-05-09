package com.dropmap.www.domain.geolocation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GeolocationInfoRepository extends JpaRepository<GeolocationInfo, GeolocationId> {
}
