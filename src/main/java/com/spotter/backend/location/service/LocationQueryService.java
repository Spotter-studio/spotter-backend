package com.spotter.backend.location.service;

import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationQueryService {

	private final LocationRepository locationRepository;

	public List<LocationDTO.Response> search(String query) {
		if (!StringUtils.hasText(query)) {
			return List.of();
		}

		String normalized = query.trim();
		// 장소 검색은 내부 DB의 이름/주소를 기준으로 수행합니다.
		return locationRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(normalized, normalized)
			.stream()
			.map(LocationMapper::toResponse)
			.toList();
	}

	public LocationDTO.Response get(Long locationId) {
		return locationRepository.findById(locationId)
			.map(LocationMapper::toResponse)
			.orElseThrow(() -> new BusinessException(ErrorCode.LOCATION_NOT_FOUND));
	}
}
