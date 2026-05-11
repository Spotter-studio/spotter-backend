package com.spotter.backend.scrap.repository;

import com.spotter.backend.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

	boolean existsByUser_IdAndLocation_Id(Long userId, Long locationId);

	List<Scrap> findByUser_Id(Long userId);

	void deleteByUser_Id(Long userId);
}
