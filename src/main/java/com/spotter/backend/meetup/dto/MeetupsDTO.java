package com.spotter.backend.meetup.dto;

import com.spotter.backend.common.enums.MeetupStatus;
import com.spotter.backend.common.enums.MeetupVisibility;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class MeetupsDTO {

	private MeetupsDTO() {
	}

	public record CreateRequest(
		@NotNull
		Long locationId,

		@NotBlank
		@Size(max = 200)
		String title,

		String description,

		@NotNull
		@Future
		LocalDateTime meetupAt,

		@Min(2)
		Integer maxParticipants,

		MeetupVisibility visibility
	) {
	}

	public record UpdateRequest(
		@Size(max = 200)
		String title,

		String description,

		@Future
		LocalDateTime meetupAt,

		@Min(2)
		Integer maxParticipants,

		MeetupStatus status,

		MeetupVisibility visibility
	) {
	}

	public record Response(
		Long id,
		Long hostId,
		Long locationId,
		String title,
		String description,
		LocalDateTime meetupAt,
		Integer maxParticipants,
		MeetupStatus status,
		LocalDateTime createdAt,
		MeetupVisibility visibility
	) {
	}
}
