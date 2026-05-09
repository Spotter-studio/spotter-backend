package com.spotter.backend.meetup.service;

import com.spotter.backend.meetup.dto.MeetupInvitationsDTO;
import com.spotter.backend.meetup.repository.MeetupInvitationsRepository;
import com.spotter.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetupInvitationsQueryService {

	private final MeetupInvitationsRepository meetupInvitationsRepository;
	private final MeetupsServiceHelper helper;

	public List<MeetupInvitationsDTO.Response> getIncoming(String email) {
		User user = helper.findUser(email);
		return meetupInvitationsRepository.findAllByUser_Id(user.getId())
			.stream()
			.map(MeetupsMapper::toResponse)
			.toList();
	}
}
