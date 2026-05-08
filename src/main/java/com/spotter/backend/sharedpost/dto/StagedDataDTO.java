package com.spotter.backend.sharedpost.dto;

import com.spotter.backend.common.enums.SourceType;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class StagedDataDTO {

	private StagedDataDTO() {
	}

	public record CreateRequest(
		@Size(max = 1000)
		String sourceUrl,

		SourceType sourceType
	) {
	}

	public record UpdateRequest(
		@Size(max = 1000)
		String sourceUrl,

		SourceType sourceType
	) {
	}

	public record Response(
		Long id,
		Long userId,
		String sourceUrl,
		SourceType sourceType,
		LocalDateTime createdAt
	) {
	}
}
