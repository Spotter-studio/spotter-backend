package com.spotter.backend.scrap.dto;

import com.spotter.backend.common.enums.SourceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class ScrapDTO {

	private ScrapDTO() {
	}

	public record CreateRequest(
		@NotNull
		Long locationId,

		@Size(max = 1000)
		String sourceUrl,

		SourceType sourceType
	) {
	}

	public record UpdateRequest(
		@Size(max = 1000)
		String sourceUrl,

		SourceType sourceType,

		@Min(0)
		Integer visitCount
	) {
	}

	public record Response(
		Long id,
		Long userId,
		Long locationId,
		String sourceUrl,
		SourceType sourceType,
		Integer visitCount,
		LocalDateTime createdAt
	) {
	}
}
