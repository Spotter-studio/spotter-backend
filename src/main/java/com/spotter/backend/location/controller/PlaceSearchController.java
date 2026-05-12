package com.spotter.backend.location.controller;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.location.controller.docs.PlaceSearchControllerDocs;
import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.location.service.LocationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceSearchController implements PlaceSearchControllerDocs {

	private final LocationQueryService locationQueryService;

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<LocationDTO.Response>>> search(@RequestParam("query") String query) {
		return ResponseEntity.ok(ApiResponse.onSuccess(locationQueryService.search(query)));
	}
}
