package com.spotter.backend.common.docs;

import com.spotter.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Health", description = "헬스 체크 API(Front 사용 x)")
public interface HealthControllerDocs {

	@Operation(summary = "헬스 체크")
	ResponseEntity<ApiResponse<Void>> health();
}
