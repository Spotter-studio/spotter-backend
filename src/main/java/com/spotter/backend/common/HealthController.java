package com.spotter.backend.common;

import com.spotter.backend.common.docs.HealthControllerDocs;
import com.spotter.backend.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController implements HealthControllerDocs {

	@GetMapping
	public ResponseEntity<ApiResponse<Void>> health() {
		return ResponseEntity.ok(ApiResponse.onSuccess());
	}
}
