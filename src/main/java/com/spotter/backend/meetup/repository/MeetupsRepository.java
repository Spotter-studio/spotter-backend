package com.spotter.backend.meetup.repository;

import com.spotter.backend.meetup.entity.Meetups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupsRepository extends JpaRepository<Meetups, Long> {
}
