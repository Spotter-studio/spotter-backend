package com.spotter.backend.category.controller;

import com.spotter.backend.category.dto.CategoryDTO;
import com.spotter.backend.category.service.CategoryQueryService;
import com.spotter.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Categories", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriesController {

	private final CategoryQueryService categoryQueryService;

	@Operation(summary = "카테고리 목록 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryDTO.Response>>> list() {
		return ResponseEntity.ok(ApiResponse.onSuccess(categoryQueryService.list()));
	}
}
