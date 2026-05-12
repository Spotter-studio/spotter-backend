package com.spotter.backend.scrap.controller;

import com.spotter.backend.auth.AuthenticatedUser;
import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.scrap.controller.docs.ScrapsControllerDocs;
import com.spotter.backend.scrap.dto.ScrapDTO;
import com.spotter.backend.scrap.service.ScrapCommandService;
import com.spotter.backend.scrap.service.ScrapQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scraps")
@RequiredArgsConstructor
public class ScrapsController implements ScrapsControllerDocs {

	private final ScrapQueryService scrapQueryService;
	private final ScrapCommandService scrapCommandService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<ScrapDTO.Response>>> list(
		Authentication authentication,
		@RequestParam(required = false) Integer categoryId
	) {
		return ResponseEntity.ok(ApiResponse.onSuccess(
			scrapQueryService.list(AuthenticatedUser.id(authentication), categoryId)
		));
	}

	@PatchMapping("/{scrapId}")
	public ResponseEntity<ApiResponse<ScrapDTO.Response>> update(
		Authentication authentication,
		@PathVariable Long scrapId,
		@Valid @RequestBody ScrapDTO.UpdateRequest request
	) {
		return ResponseEntity.ok(ApiResponse.onSuccess(
			scrapCommandService.update(AuthenticatedUser.id(authentication), scrapId, request)
		));
	}

	@DeleteMapping("/{scrapId}")
	public ResponseEntity<Void> delete(
		Authentication authentication,
		@PathVariable Long scrapId
	) {
		scrapCommandService.delete(AuthenticatedUser.id(authentication), scrapId);
		return ResponseEntity.noContent().build();
	}
}
