package com.spotter.backend.meetup.repository;

import com.spotter.backend.meetup.entity.MeetupInvitations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupInvitationsRepository extends JpaRepository<MeetupInvitations, Long> {

	boolean existsByMeetup_IdAndUser_Id(Long meetupId, Long userId);
}
