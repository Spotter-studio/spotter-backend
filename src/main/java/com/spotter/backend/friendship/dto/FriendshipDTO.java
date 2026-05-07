package com.spotter.backend.friendship.dto;

import com.spotter.backend.common.enums.FriendshipStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class FriendshipDTO {

	private FriendshipDTO() {
	}

	public record CreateRequest(
		@NotNull
		Long friendId
	) {
	}

	public record UpdateRequest(
		@NotNull
		FriendshipStatus status
	) {
	}

	public record Response(
		Long id,
		Long userId,
		Long friendId,
		FriendshipStatus status,
		LocalDateTime createdAt,
		LocalDateTime acceptedAt
	) {
	}
}
