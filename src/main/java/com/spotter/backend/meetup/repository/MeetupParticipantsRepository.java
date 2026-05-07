package com.spotter.backend.meetup.repository;

import com.spotter.backend.meetup.entity.MeetupParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupParticipantsRepository extends JpaRepository<MeetupParticipants, Long> {

	boolean existsByMeetup_IdAndUser_Id(Long meetupId, Long userId);
}
