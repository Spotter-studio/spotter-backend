package com.spotter.backend.location.controller;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.location.controller.docs.LocationControllerDocs;
import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.location.service.LocationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController implements LocationControllerDocs {

	private final LocationQueryService locationQueryService;

	@GetMapping("/{locationId}")
	public ResponseEntity<ApiResponse<LocationDTO.Response>> get(@PathVariable Long locationId) {
		return ResponseEntity.ok(ApiResponse.onSuccess(locationQueryService.get(locationId)));
	}
}
