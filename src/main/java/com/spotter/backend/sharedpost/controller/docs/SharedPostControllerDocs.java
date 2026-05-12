package com.spotter.backend.sharedpost.controller.docs;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Shared Posts", description = "공유 게시글 관련 API")
@SecurityRequirement(name = "bearerAuth")
public interface SharedPostControllerDocs {

	@Operation(summary = "공유 게시글 생성")
	ResponseEntity<ApiResponse<SharedPostDTO.Response>> create(Authentication authentication, @Valid SharedPostDTO.CreateRequest request, List<MultipartFile> images);

	@Operation(summary = "대기 중인 공유 게시글 목록 조회")
	ResponseEntity<ApiResponse<List<SharedPostDTO.Response>>> getPending(Authentication authentication);

	@Operation(summary = "공유 게시글 확정")
	ResponseEntity<ApiResponse<SharedPostDTO.Response>> confirm(Authentication authentication, Long postId, @Valid @RequestBody SharedPostDTO.ConfirmRequest request);

	@Operation(summary = "공유 게시글 삭제")
	ResponseEntity<ApiResponse<Void>> delete(Authentication authentication, Long postId);
}
