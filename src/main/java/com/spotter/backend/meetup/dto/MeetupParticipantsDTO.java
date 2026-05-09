package com.spotter.backend.meetup.dto;

import java.time.LocalDateTime;

public final class MeetupParticipantsDTO {

	private MeetupParticipantsDTO() {
	}

	public record Response(
		Long id,
		Long userId,
		Long meetupId,
		LocalDateTime createdAt
	) {
	}
}
