package com.spotter.backend.meetup.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public final class MeetupParticipantsDTO {

	private MeetupParticipantsDTO() {
	}

	public record CreateRequest(
		@NotNull
		Long userId,

		@NotNull
		Long meetupId
	) {
	}

	public record UpdateRequest() {
	}

	public record Response(
		Long id,
		Long userId,
		Long meetupId,
		LocalDateTime joinedAt
	) {
	}
}
