package com.spotter.backend.meetup.repository;

import com.spotter.backend.meetup.entity.MeetupParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetupParticipantsRepository extends JpaRepository<MeetupParticipants, Long> {

	interface ParticipantCount {
		Long getMeetupId();
		Long getCount();
	}

	boolean existsByMeetup_IdAndUser_Id(Long meetupId, Long userId);

	long countByMeetup_Id(Long meetupId);

	@Query("SELECT p.meetup.id as meetupId, COUNT(p) as count FROM MeetupParticipants p WHERE p.meetup.id IN :meetupIds GROUP BY p.meetup.id")
	List<ParticipantCount> countByMeetupIds(@Param("meetupIds") List<Long> meetupIds);

	void deleteByMeetup_IdAndUser_Id(Long meetupId, Long userId);

	void deleteByUser_Id(Long userId);

	void deleteByMeetup_IdIn(List<Long> meetupIds);
}
