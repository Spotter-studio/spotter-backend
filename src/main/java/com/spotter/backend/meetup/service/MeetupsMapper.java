package com.spotter.backend.meetup.service;

import com.spotter.backend.meetup.dto.MeetupInvitationsDTO;
import com.spotter.backend.meetup.dto.MeetupsDTO;
import com.spotter.backend.meetup.entity.MeetupInvitations;
import com.spotter.backend.meetup.entity.Meetups;

final class MeetupsMapper {

	private MeetupsMapper() {}

	static MeetupsDTO.Response toResponse(Meetups meetup, int currentParticipants) {
		return new MeetupsDTO.Response(
			meetup.getId(),
			meetup.getHost().getId(),
			meetup.getLocation().getId(),
			meetup.getTitle(),
			meetup.getDescription(),
			meetup.getMeetupAt(),
			meetup.getMaxParticipants(),
			currentParticipants,
			meetup.getStatus(),
			meetup.getVisibility(),
			meetup.getCreatedAt()
		);
	}

	static MeetupInvitationsDTO.Response toResponse(MeetupInvitations invitation) {
		return new MeetupInvitationsDTO.Response(
			invitation.getId(),
			invitation.getInvitedBy().getId(),
			invitation.getUser().getId(),
			invitation.getMeetup().getId(),
			invitation.getStatus(),
			invitation.getCreatedAt(),
			invitation.getRespondedAt()
		);
	}
}
