package com.spotter.backend.meetup.service;

import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.meetup.dto.MeetupInvitationsDTO;
import com.spotter.backend.meetup.entity.MeetupInvitations;
import com.spotter.backend.meetup.entity.MeetupParticipants;
import com.spotter.backend.meetup.entity.Meetups;
import com.spotter.backend.meetup.repository.MeetupInvitationsRepository;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetupInvitationsCommandService {

	private final MeetupInvitationsRepository meetupInvitationsRepository;
	private final MeetupParticipantsRepository meetupParticipantsRepository;
	private final FriendshipRepository friendshipRepository;
	private final UserRepository userRepository;
	private final MeetupsServiceHelper helper;

	public MeetupInvitationsDTO.Response invite(String email, Long meetupId, MeetupInvitationsDTO.CreateRequest request) {
		User host = helper.findUser(email);
		Meetups meetup = helper.findMeetup(meetupId);
		helper.validateHost(host, meetup);

		User targetUser = userRepository.findById(request.userId())
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		if (!friendshipRepository.existsBidirectional(host.getId(), targetUser.getId(), FriendshipStatus.ACCEPTED)) {
			throw new BusinessException(ErrorCode.NOT_FRIENDS);
		}
		if (meetupInvitationsRepository.existsByMeetup_IdAndUser_Id(meetupId, targetUser.getId())) {
			throw new BusinessException(ErrorCode.INVITATION_ALREADY_SENT);
		}
		if (meetupParticipantsRepository.existsByMeetup_IdAndUser_Id(meetupId, targetUser.getId())) {
			throw new BusinessException(ErrorCode.MEETUP_ALREADY_JOINED);
		}

		MeetupInvitations invitation = meetupInvitationsRepository.save(new MeetupInvitations(host, targetUser, meetup));
		return MeetupsMapper.toResponse(invitation);
	}

	public MeetupInvitationsDTO.Response accept(String email, Long invitationId) {
		User user = helper.findUser(email);
		MeetupInvitations invitation = findInvitation(invitationId, user.getId());
		Meetups meetup = invitation.getMeetup();

		helper.validateCapacity(meetup);
		invitation.accept();
		meetupParticipantsRepository.save(new MeetupParticipants(user, meetup));
		return MeetupsMapper.toResponse(invitation);
	}

	public MeetupInvitationsDTO.Response reject(String email, Long invitationId) {
		User user = helper.findUser(email);
		MeetupInvitations invitation = findInvitation(invitationId, user.getId());
		invitation.reject();
		return MeetupsMapper.toResponse(invitation);
	}

	private MeetupInvitations findInvitation(Long invitationId, Long userId) {
		return meetupInvitationsRepository.findByIdAndUser_Id(invitationId, userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.INVITATION_NOT_FOUND));
	}
}
