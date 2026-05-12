package com.spotter.backend.sharedpost.controller;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import com.spotter.backend.sharedpost.service.SharedPostCommandService;
import com.spotter.backend.sharedpost.service.SharedPostQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Shared Posts", description = "공유 게시글 관련 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/shared-posts")
@RequiredArgsConstructor
public class SharedPostController {

	// 단순 목록만 가져오는 경우 queryService 사용
	private final SharedPostQueryService sharedPostQueryService;
	private final SharedPostCommandService sharedPostCommandService;

	@Operation(summary = "공유 게시글 생성")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<SharedPostDTO.Response>> create(
		Authentication authentication,
		@RequestPart("request") @Valid SharedPostDTO.CreateRequest request,
		@RequestPart(value = "images", required = false) List<MultipartFile> images
	) {
		return ResponseEntity.ok(ApiResponse.onSuccess(sharedPostCommandService.create(authentication.getName(), request, images)));
	}

	@Operation(summary = "대기 중인 공유 게시글 목록 조회")
	@GetMapping("/pending")
	public ResponseEntity<ApiResponse<List<SharedPostDTO.Response>>> getPending(Authentication authentication) {
		return ResponseEntity.ok(ApiResponse.onSuccess(sharedPostQueryService.getPending(authentication.getName())));
	}

	@Operation(summary = "공유 게시글 확정")
	@PostMapping("/{postId}/confirm")
	public ResponseEntity<ApiResponse<SharedPostDTO.Response>> confirm(
		Authentication authentication,
		@PathVariable Long postId,
		@Valid @RequestBody SharedPostDTO.ConfirmRequest request
	) {
		return ResponseEntity.ok(ApiResponse.onSuccess(sharedPostCommandService.confirm(authentication.getName(), postId, request)));
	}

	@Operation(summary = "공유 게시글 삭제")
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> delete(
		Authentication authentication,
		@PathVariable Long postId
	) {
		sharedPostCommandService.delete(authentication.getName(), postId);
		return ResponseEntity.ok(ApiResponse.onSuccess());
	}
}
