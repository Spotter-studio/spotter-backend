package com.spotter.backend.meetup.dto;

import com.spotter.backend.common.enums.InvitationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class MeetupInvitationsDTO {

	private MeetupInvitationsDTO() {
	}

	public record CreateRequest(
		@NotNull
		Long userId
	) {
	}

	public record Response(
		Long id,
		Long invitedBy,
		Long userId,
		Long meetupId,
		InvitationStatus status,
		LocalDateTime createdAt,
		LocalDateTime respondedAt
	) {
	}
}
