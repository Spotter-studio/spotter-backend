package com.spotter.backend.sharedpost.dto;

import com.spotter.backend.common.enums.SharedPostStatus;
import com.spotter.backend.common.enums.SourceType;
import com.spotter.backend.location.dto.LocationDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public final class SharedPostDTO {

	private SharedPostDTO() {
	}

	public record CreateRequest(
		@Size(max = 1000)
		String sourceUrl,

		@NotNull
		SourceType sourceType,

		String ocrText,

		List<@Size(max = 1000) String> imageUrls
	) {
	}

	public record LocationInput(
		Long id,
		String naverPlaceId,
		String name,
		String address,
		BigDecimal latitude,
		BigDecimal longitude,
		Integer categoryId
	) {
	}

	public record ConfirmRequest(
		@NotNull List<LocationInput> locations
	) {
	}

	public record Response(
		Long id,
		Long userId,
		String sourceUrl,
		SourceType sourceType,
		SharedPostStatus status,
		List<LocationDTO.Response> locations,
		LocalDateTime createdAt
	) {
	}
}
