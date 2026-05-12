package com.spotter.backend.common;

import com.spotter.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "헬스 체크 API")
@RestController
@RequestMapping("/api/health")
public class HealthController {

	@Operation(summary = "헬스 체크")
	@GetMapping
	public ResponseEntity<ApiResponse<Void>> health() {
		return ResponseEntity.ok(ApiResponse.onSuccess());
	}
}
