package com.spotter.backend.sharedpost.repository;

import com.spotter.backend.common.enums.SharedPostStatus;
import com.spotter.backend.sharedpost.entity.SharedPost;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedPostRepository extends JpaRepository<SharedPost, Long> {

	@EntityGraph(attributePaths = {"locations", "locations.category"})
	List<SharedPost> findByUser_Id(Long userId);

	void deleteByUser_Id(Long userId);
  
	@EntityGraph(attributePaths = {"locations", "locations.category"})
	List<SharedPost> findByUser_IdAndStatus(Long userId, SharedPostStatus status);
}
