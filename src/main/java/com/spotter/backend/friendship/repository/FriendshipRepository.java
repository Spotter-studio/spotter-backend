package com.spotter.backend.friendship.repository;

import com.spotter.backend.friendship.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	boolean existsByUser_IdAndFriend_Id(Long userId, Long friendId);
}
