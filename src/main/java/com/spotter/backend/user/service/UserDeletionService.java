package com.spotter.backend.user.service;

import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.meetup.entity.Meetups;
import com.spotter.backend.meetup.repository.MeetupInvitationsRepository;
import com.spotter.backend.meetup.repository.MeetupParticipantsRepository;
import com.spotter.backend.meetup.repository.MeetupsRepository;
import com.spotter.backend.scrap.repository.ScrapRepository;
import com.spotter.backend.sharedpost.repository.SharedPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserDeletionService {

	private final JdbcTemplate jdbcTemplate;
	private final FriendshipRepository friendshipRepository;
	private final MeetupInvitationsRepository meetupInvitationsRepository;
	private final MeetupParticipantsRepository meetupParticipantsRepository;
	private final MeetupsRepository meetupsRepository;
	private final ScrapRepository scrapRepository;
	private final SharedPostRepository sharedPostRepository;

	void deleteUserRelations(Long userId) {
		List<Long> hostedMeetupIds = meetupsRepository.findAllByHost_Id(userId).stream()
			.peek(Meetups::cancel)
			.map(Meetups::getId)
			.toList();

		deleteSharedPostChildren(userId);
		sharedPostRepository.deleteByUser_Id(userId);

		decrementScrapCounts(userId);
		deleteScrapSourceUrls(userId);
		scrapRepository.deleteByUser_Id(userId);

		meetupInvitationsRepository.deleteByInvitedBy_IdOrUser_Id(userId, userId);
		meetupParticipantsRepository.deleteByUser_Id(userId);
		friendshipRepository.deleteByUser_IdOrFriend_Id(userId, userId);

		if (!hostedMeetupIds.isEmpty()) {
			meetupInvitationsRepository.deleteByMeetup_IdIn(hostedMeetupIds);
			meetupParticipantsRepository.deleteByMeetup_IdIn(hostedMeetupIds);
			meetupsRepository.deleteByHost_Id(userId);
		}
	}

	private void deleteSharedPostChildren(Long userId) {
		jdbcTemplate.update("""
			DELETE FROM shared_post_image
			WHERE shared_post_id IN (SELECT id FROM shared_post WHERE user_id = ?)
			""", userId);
		jdbcTemplate.update("""
			DELETE FROM shared_post_location
			WHERE shared_post_id IN (SELECT id FROM shared_post WHERE user_id = ?)
			""", userId);
	}

	private void deleteScrapSourceUrls(Long userId) {
		jdbcTemplate.update("""
			DELETE FROM scrap_source_url
			WHERE scrap_id IN (SELECT id FROM scrap WHERE user_id = ?)
			""", userId);
	}

	private void decrementScrapCounts(Long userId) {
		jdbcTemplate.update("""
			UPDATE location
			SET total_scrap_count = GREATEST(total_scrap_count - deleted_scraps.count, 0)
			FROM (
				SELECT location_id, COUNT(*) AS count
				FROM scrap
				WHERE user_id = ?
				GROUP BY location_id
			) deleted_scraps
			WHERE location.id = deleted_scraps.location_id
			""", userId);
	}
}
