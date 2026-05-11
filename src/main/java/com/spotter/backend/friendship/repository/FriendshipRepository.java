package com.spotter.backend.friendship.repository;

import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.friendship.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	boolean existsByUser_IdAndFriend_Id(Long userId, Long friendId);

	// 양방향 친구 관계 확인 (A→B 또는 B→A 방향 모두 검사)
	@Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END FROM Friendship f WHERE " +
		"((f.user.id = :userId AND f.friend.id = :otherId) OR (f.user.id = :otherId AND f.friend.id = :userId)) " +
		"AND f.status = :status")
	boolean existsBidirectional(@Param("userId") Long userId, @Param("otherId") Long otherId, @Param("status") FriendshipStatus status);

	List<Friendship> findAllByUser_IdAndStatus(Long userId, FriendshipStatus status);

	List<Friendship> findAllByFriend_IdAndStatus(Long friendId, FriendshipStatus status);

	void deleteByUser_IdOrFriend_Id(Long userId, Long friendId);
}
