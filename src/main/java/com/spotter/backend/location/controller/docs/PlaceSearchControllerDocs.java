package com.spotter.backend.location.controller.docs;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.location.dto.LocationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Places", description = "장소 검색 API")
@SecurityRequirement(name = "bearerAuth")
public interface PlaceSearchControllerDocs {

	@Operation(summary = "장소 검색")
	ResponseEntity<ApiResponse<List<LocationDTO.Response>>> search(String query);
}
