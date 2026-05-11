package com.spotter.backend.sharedpost.controller;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import com.spotter.backend.sharedpost.service.SharedPostCommandService;
import com.spotter.backend.sharedpost.service.SharedPostQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shared-posts")
@RequiredArgsConstructor
public class SharedPostController {

	// 단순 목록만 가져오는 경우 queryService 사용
	private final SharedPostQueryService sharedPostQueryService;
	private final SharedPostCommandService sharedPostCommandService;

	@PostMapping
	public ResponseEntity<ApiResponse<SharedPostDTO.Response>> create(
		Authentication authentication,
		@Valid @RequestBody SharedPostDTO.CreateRequest request
	) {
		return ResponseEntity.ok(ApiResponse.onSuccess(sharedPostCommandService.create(authentication.getName(), request)));
	}

	@GetMapping("/pending")
	public ResponseEntity<ApiResponse<List<SharedPostDTO.Response>>> getPending(Authentication authentication) {
		return ResponseEntity.ok(ApiResponse.onSuccess(sharedPostQueryService.getPending(authentication.getName())));
	}

	@PostMapping("/{postId}/confirm")
	public ResponseEntity<ApiResponse<SharedPostDTO.Response>> confirm(
		Authentication authentication,
		@PathVariable Long postId,
		@Valid @RequestBody SharedPostDTO.ConfirmRequest request
	) {
		return ResponseEntity.ok(ApiResponse.onSuccess(sharedPostCommandService.confirm(authentication.getName(), postId, request)));
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> delete(
		Authentication authentication,
		@PathVariable Long postId
	) {
		sharedPostCommandService.delete(authentication.getName(), postId);
		return ResponseEntity.ok(ApiResponse.onSuccess());
	}
}
