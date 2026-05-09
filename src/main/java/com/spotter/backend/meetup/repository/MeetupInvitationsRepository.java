package com.spotter.backend.meetup.repository;

import com.spotter.backend.meetup.entity.MeetupInvitations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetupInvitationsRepository extends JpaRepository<MeetupInvitations, Long> {

	boolean existsByMeetup_IdAndUser_Id(Long meetupId, Long userId);

	List<MeetupInvitations> findAllByUser_Id(Long userId);

	Optional<MeetupInvitations> findByIdAndUser_Id(Long id, Long userId);
}
