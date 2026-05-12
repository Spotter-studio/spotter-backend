package com.spotter.backend.friendship.entity;

import com.spotter.backend.common.converter.FriendshipStatusConverter;
import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
	name = "friendship",
	uniqueConstraints = @UniqueConstraint(name = "uk_friendship_user_friend", columnNames = {"user_id", "friend_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Friendship {

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

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "accepted_at")
	private LocalDateTime acceptedAt;

	public static Friendship of(User user, User friend) {
		Friendship friendship = new Friendship();
		friendship.user = user;
		friendship.friend = friend;
		return friendship;
	}

	public void accept() {
		this.status = FriendshipStatus.ACCEPTED;
		this.acceptedAt = LocalDateTime.now();
	}

	public void reject() {
		this.status = FriendshipStatus.REJECTED;
	}
}
