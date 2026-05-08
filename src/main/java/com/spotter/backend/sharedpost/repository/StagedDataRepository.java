package com.spotter.backend.sharedpost.repository;

import com.spotter.backend.sharedpost.entity.SharedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StagedDataRepository extends JpaRepository<SharedPost, Long> {

	List<SharedPost> findByUser_Id(Long userId);
}
