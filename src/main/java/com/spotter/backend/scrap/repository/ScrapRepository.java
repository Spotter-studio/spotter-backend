package com.spotter.backend.scrap.repository;

import com.spotter.backend.location.entity.Location;
import com.spotter.backend.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

	boolean existsByUser_IdAndLocation_Id(Long userId, Long locationId);

	Optional<Scrap> findByUser_IdAndLocation_Id(Long userId, Long locationId);

	List<Scrap> findByUser_Id(Long userId);

	void deleteByUser_Id(Long userId);

	// 두 유저간 공통 장소 조회
	@Query("SELECT s.location FROM Scrap s WHERE s.user.id = :userId AND s.location.id IN " +
		"(SELECT s2.location.id FROM Scrap s2 WHERE s2.user.id = :friendId)")
	List<Location> findCommonLocations(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
