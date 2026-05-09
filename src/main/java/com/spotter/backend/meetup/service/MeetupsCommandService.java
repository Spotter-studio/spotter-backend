package com.spotter.backend.meetup.service;

import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.common.enums.MeetupStatus;
import com.spotter.backend.common.enums.MeetupVisibility;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.location.entity.Location;
import com.spotter.backend.location.repository.LocationRepository;
import com.spotter.backend.meetup.dto.MeetupsDTO;
import com.spotter.backend.meetup.entity.MeetupParticipants;
import com.spotter.backend.meetup.entity.Meetups;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository;
import com.spotter.backend.meetup.repository.MeetupsRepository;
import com.spotter.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetupsCommandService {

	private final MeetupsRepository meetupsRepository;
	private final MeetupParticipantsRepository meetupParticipantsRepository;
	private final LocationRepository locationRepository;
	private final FriendshipRepository friendshipRepository;
	private final MeetupsServiceHelper helper;

	public MeetupsDTO.Response create(String email, MeetupsDTO.CreateRequest request) {
		User user = helper.findUser(email);
		Location location = locationRepository.findById(request.locationId())
			.orElseThrow(() -> new BusinessException(ErrorCode.LOCATION_NOT_FOUND));

		Meetups meetup = meetupsRepository.save(
			new Meetups(user, location, request.title(), request.description(),
				request.meetupAt(), request.maxParticipants(), request.visibility()));
		meetupParticipantsRepository.save(new MeetupParticipants(user, meetup));

		return MeetupsMapper.toResponse(meetup, 1);
	}

	public MeetupsDTO.Response join(String email, Long meetupId) {
		User user = helper.findUser(email);
		Meetups meetup = helper.findMeetup(meetupId);

		if (meetup.getStatus() != MeetupStatus.RECRUITING) {
			throw new BusinessException(ErrorCode.MEETUP_NOT_RECRUITING);
		}
		if (meetupParticipantsRepository.existsByMeetup_IdAndUser_Id(meetupId, user.getId())) {
			throw new BusinessException(ErrorCode.MEETUP_ALREADY_JOINED);
		}

		if (meetup.getVisibility() == MeetupVisibility.PRIVATE) {
			throw new BusinessException(ErrorCode.MEETUP_ACCESS_DENIED);
		}
		if (meetup.getVisibility() == MeetupVisibility.FRIENDS_ONLY
			&& !friendshipRepository.existsBidirectional(user.getId(), meetup.getHost().getId(), FriendshipStatus.ACCEPTED)) {
			throw new BusinessException(ErrorCode.MEETUP_ACCESS_DENIED);
		}

		long currentCount = helper.validateCapacity(meetup);
		meetupParticipantsRepository.save(new MeetupParticipants(user, meetup));
		return MeetupsMapper.toResponse(meetup, (int) (currentCount + 1));
	}

	public void cancel(String email, Long meetupId) {
		User user = helper.findUser(email);
		Meetups meetup = helper.findMeetup(meetupId);
		helper.validateHost(user, meetup);
		meetup.cancel();
	}

	public void leave(String email, Long meetupId) {
		User user = helper.findUser(email);
		Meetups meetup = helper.findMeetup(meetupId);

		if (meetup.getHost().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCode.MEETUP_HOST_CANNOT_LEAVE);
		}
		if (!meetupParticipantsRepository.existsByMeetup_IdAndUser_Id(meetupId, user.getId())) {
			throw new BusinessException(ErrorCode.MEETUP_NOT_PARTICIPANT);
		}
		meetupParticipantsRepository.deleteByMeetup_IdAndUser_Id(meetupId, user.getId());
	}
}
