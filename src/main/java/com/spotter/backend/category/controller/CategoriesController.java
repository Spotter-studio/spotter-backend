package com.spotter.backend.category.controller;

import com.spotter.backend.category.dto.CategoryDTO;
import com.spotter.backend.category.service.CategoryQueryService;
import com.spotter.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriesController {

	private final CategoryQueryService categoryQueryService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CategoryDTO.Response>>> list() {
		return ResponseEntity.ok(ApiResponse.onSuccess(categoryQueryService.list()));
	}
}
