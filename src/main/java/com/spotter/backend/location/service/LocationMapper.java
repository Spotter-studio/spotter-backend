package com.spotter.backend.location.service;

import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.location.entity.Location;

final class LocationMapper {

	private LocationMapper() {
	}

	static LocationDTO.Response toResponse(Location location) {
		return new LocationDTO.Response(
			location.getId(),
			location.getLatitude(),
			location.getLongitude(),
			location.getCategory().getId(),
			location.getCategory().getName(),
			location.getNaverPlaceId(),
			location.getName(),
			location.getAddress(),
			location.getTotalScrapCount(),
			location.getCreatedAt(),
			location.getUpdatedAt()
		);
	}
}
