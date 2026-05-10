package com.spotter.backend.meetup.entity;

import com.spotter.backend.common.entity.BaseTimeEntity;
import com.spotter.backend.user.entity.User;
import jakarta.persistence.Column;
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
	name = "meetup_participants",
	uniqueConstraints = @UniqueConstraint(name = "uk_meetup_participants_meetup_user", columnNames = {"meetup_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetupParticipants extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "meetup_id", nullable = false)
	private Meetups meetup;

	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;

	public MeetupParticipants(User user, Meetups meetup) {
		this.user = user;
		this.meetup = meetup;
		this.joinedAt = LocalDateTime.now();
	}
}
