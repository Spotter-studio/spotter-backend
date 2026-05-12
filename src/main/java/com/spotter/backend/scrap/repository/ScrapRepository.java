package com.spotter.backend.scrap.repository;

import com.spotter.backend.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

	boolean existsByUser_IdAndLocation_Id(Long userId, Long locationId);

	Optional<Scrap> findByUser_IdAndLocation_Id(Long userId, Long locationId);

	@Query("""
		SELECT s FROM Scrap s
		JOIN FETCH s.user
		JOIN FETCH s.location l
		JOIN FETCH l.category
		WHERE s.user.id = :userId
		  AND (:categoryId IS NULL OR l.category.id = :categoryId)
		""")
	List<Scrap> findByUserIdWithFetchJoins(
		@Param("userId") Long userId,
		@Param("categoryId") Integer categoryId
	);

	@Query("""
		SELECT s FROM Scrap s
		JOIN FETCH s.user
		JOIN FETCH s.location l
		JOIN FETCH l.category
		WHERE s.id = :scrapId
		""")
	Optional<Scrap> findByIdWithFetchJoins(@Param("scrapId") Long scrapId);

	void deleteByUser_Id(Long userId);
}
