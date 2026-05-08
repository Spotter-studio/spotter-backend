package com.spotter.backend.friendship.entity;

import com.spotter.backend.common.converter.FriendshipStatusConverter;
import com.spotter.backend.common.entity.BaseEntity;
import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
	name = "friendship",
	uniqueConstraints = @UniqueConstraint(name = "uk_friendship_user_friend", columnNames = {"user_id", "friend_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friendship extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "friend_id", nullable = false)
	private User friend;

	@Convert(converter = FriendshipStatusConverter.class)
	@Column(nullable = false)
	private FriendshipStatus status = FriendshipStatus.PENDING;

	@Column(name = "accepted_at")
	private LocalDateTime acceptedAt;
}
