package com.spotter.backend.location.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class LocationDTO {

	private LocationDTO() {
	}

	public record CreateRequest(
		@NotNull
		@DecimalMin("-90.0")
		@DecimalMax("90.0")
		BigDecimal latitude,

		@NotNull
		@DecimalMin("-180.0")
		@DecimalMax("180.0")
		BigDecimal longitude,

		@NotNull
		Integer categoryId,

		@Size(max = 100)
		String naverPlaceId,

		@NotBlank
		@Size(max = 255)
		String name,

		@Size(max = 500)
		String address
	) {
	}

	public record UpdateRequest(
		@DecimalMin("-90.0")
		@DecimalMax("90.0")
		BigDecimal latitude,

		@DecimalMin("-180.0")
		@DecimalMax("180.0")
		BigDecimal longitude,

		Integer categoryId,

		@Size(max = 100)
		String naverPlaceId,

		@Size(max = 255)
		String name,

		@Size(max = 500)
		String address
	) {
	}

	public record Response(
		Long id,
		BigDecimal latitude,
		BigDecimal longitude,
		Integer categoryId,
		String categoryName,
		String naverPlaceId,
		String name,
		String address,
		Integer totalScrapCount,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
	}
}
