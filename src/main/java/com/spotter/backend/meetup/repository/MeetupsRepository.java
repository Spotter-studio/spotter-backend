package com.spotter.backend.meetup.repository;

import com.spotter.backend.common.enums.MeetupVisibility;
import com.spotter.backend.meetup.entity.Meetups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetupsRepository extends JpaRepository<Meetups, Long> {

	List<Meetups> findAllByVisibility(MeetupVisibility visibility);

	List<Meetups> findAllByVisibilityAndHost_IdIn(MeetupVisibility visibility, List<Long> hostIds);

	List<Meetups> findAllByHost_Id(Long hostId);

	void deleteByHost_Id(Long hostId);
}
