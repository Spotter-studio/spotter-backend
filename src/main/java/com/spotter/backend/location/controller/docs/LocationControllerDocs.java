package com.spotter.backend.location.controller.docs;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.location.dto.LocationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Locations", description = "장소 정보 API")
@SecurityRequirement(name = "bearerAuth")
public interface LocationControllerDocs {

	@Operation(summary = "장소 상세 조회")
	ResponseEntity<ApiResponse<LocationDTO.Response>> get(Long locationId);
}
