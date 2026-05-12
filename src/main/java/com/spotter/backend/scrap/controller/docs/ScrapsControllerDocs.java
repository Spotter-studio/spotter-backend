package com.spotter.backend.scrap.controller.docs;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.scrap.dto.ScrapDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Scraps", description = "스크랩 관련 API")
@SecurityRequirement(name = "bearerAuth")
public interface ScrapsControllerDocs {

	@Operation(summary = "스크랩 목록 조회", description = "categoryId 파라미터가 있으면 해당 카테고리의 스크랩만 반환합니다.")
	ResponseEntity<ApiResponse<List<ScrapDTO.Response>>> list(
		Authentication authentication,
		@Parameter(description = "카테고리 ID (미입력 시 사용자 기준 전체 조회)")
		@RequestParam(required = false) Integer categoryId
	);

	@Operation(summary = "스크랩 정보 수정")
	ResponseEntity<ApiResponse<ScrapDTO.Response>> update(Authentication authentication, Long scrapId, ScrapDTO.UpdateRequest request);

	@Operation(summary = "스크랩 삭제")
	ResponseEntity<Void> delete(Authentication authentication, Long scrapId);
}
