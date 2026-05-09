package com.spotter.backend.meetup.service;

import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.meetup.entity.Meetups;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository;
import com.spotter.backend.meetup.repository.MeetupsRepository;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MeetupsServiceHelper {

	private final UserRepository userRepository;
	private final MeetupsRepository meetupsRepository;
	private final MeetupParticipantsRepository meetupParticipantsRepository;

	User findUser(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
	}

	Meetups findMeetup(Long meetupId) {
		return meetupsRepository.findById(meetupId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEETUP_NOT_FOUND));
	}

	void validateHost(User user, Meetups meetup) {
		if (!meetup.getHost().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCode.MEETUP_NOT_HOST);
		}
	}

	long validateCapacity(Meetups meetup) {
		long currentCount = meetupParticipantsRepository.countByMeetup_Id(meetup.getId());
		if (currentCount >= meetup.getMaxParticipants()) {
			throw new BusinessException(ErrorCode.MEETUP_FULL);
		}
		return currentCount;
	}
}
