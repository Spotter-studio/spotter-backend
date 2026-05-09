package com.spotter.backend.meetup.service;

import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.common.enums.MeetupVisibility;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.friendship.entity.Friendship;
import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.meetup.dto.MeetupsDTO;
import com.spotter.backend.meetup.entity.Meetups;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository.ParticipantCount;
import com.spotter.backend.meetup.repository.MeetupsRepository;
import com.spotter.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetupsQueryService {

	private final MeetupsRepository meetupsRepository;
	private final MeetupParticipantsRepository meetupParticipantsRepository;
	private final FriendshipRepository friendshipRepository;
	private final MeetupsServiceHelper helper;

	public List<MeetupsDTO.Response> list(String email) {
		User user = helper.findUser(email);
		List<Long> friendIds = getFriendIds(user.getId());

		List<Meetups> meetups = new ArrayList<>(meetupsRepository.findAllByVisibility(MeetupVisibility.PUBLIC));
		if (!friendIds.isEmpty()) {
			meetups.addAll(meetupsRepository.findAllByVisibilityAndHost_IdIn(MeetupVisibility.FRIENDS_ONLY, friendIds));
		}

		Set<Long> seen = meetups.stream().map(Meetups::getId).collect(Collectors.toSet());
		meetupsRepository.findAllByHost_Id(user.getId()).stream()
			.filter(m -> !seen.contains(m.getId()))
			.forEach(meetups::add);

		List<Long> meetupIds = meetups.stream().map(Meetups::getId).toList();
		Map<Long, Integer> countMap = meetupParticipantsRepository.countByMeetupIds(meetupIds).stream()
			.collect(Collectors.toMap(ParticipantCount::getMeetupId, r -> r.getCount().intValue()));

		return meetups.stream()
			.map(m -> MeetupsMapper.toResponse(m, countMap.getOrDefault(m.getId(), 0)))
			.toList();
	}

	public MeetupsDTO.Response get(String email, Long meetupId) {
		User user = helper.findUser(email);
		Meetups meetup = helper.findMeetup(meetupId);
		checkAccess(user, meetup);
		return MeetupsMapper.toResponse(meetup, (int) meetupParticipantsRepository.countByMeetup_Id(meetupId));
	}

	private void checkAccess(User user, Meetups meetup) {
		if (meetup.getVisibility() == MeetupVisibility.PUBLIC) return;
		if (meetup.getHost().getId().equals(user.getId())) return;
		if (meetupParticipantsRepository.existsByMeetup_IdAndUser_Id(meetup.getId(), user.getId())) return;

		if (meetup.getVisibility() == MeetupVisibility.FRIENDS_ONLY
			&& friendshipRepository.existsBidirectional(user.getId(), meetup.getHost().getId(), FriendshipStatus.ACCEPTED)) {
			return;
		}

		throw new BusinessException(ErrorCode.MEETUP_ACCESS_DENIED);
	}

	private List<Long> getFriendIds(Long userId) {
		Stream<Long> asUser = friendshipRepository.findAllByUser_IdAndStatus(userId, FriendshipStatus.ACCEPTED)
			.stream().map(f -> f.getFriend().getId());
		Stream<Long> asFriend = friendshipRepository.findAllByFriend_IdAndStatus(userId, FriendshipStatus.ACCEPTED)
			.stream().map(Friendship::getUser).map(User::getId);
		return Stream.concat(asUser, asFriend).toList();
	}
}
