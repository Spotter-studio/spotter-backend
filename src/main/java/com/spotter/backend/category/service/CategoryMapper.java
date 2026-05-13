package com.spotter.backend.category.service;

import com.spotter.backend.category.dto.CategoryDTO;
import com.spotter.backend.category.entity.Category;

public final class CategoryMapper {

	private CategoryMapper() {
	}

	public static CategoryDTO.Response toResponse(Category category) {
		return new CategoryDTO.Response(category.getId(), category.getName());
	}
}
