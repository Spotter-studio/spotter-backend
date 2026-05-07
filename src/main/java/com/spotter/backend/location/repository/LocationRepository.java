package com.spotter.backend.location.repository;

import com.spotter.backend.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByNaverPlaceId(String naverPlaceId);
}
