package com.spotter.backend.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class CategoryDTO {

	private CategoryDTO() {
	}

	public record CreateRequest(
		@NotNull
		Integer id,

		@NotBlank
		@Size(max = 50)
		String name
	) {
	}

	public record UpdateRequest(
		@NotBlank
		@Size(max = 50)
		String name
	) {
	}

	public record Response(
		Integer id,
		String name
	) {
	}
}
