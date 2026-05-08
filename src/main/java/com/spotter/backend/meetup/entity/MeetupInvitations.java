package com.spotter.backend.meetup.entity;

import com.spotter.backend.common.converter.InvitationStatusConverter;
import com.spotter.backend.common.entity.BaseEntity;
import com.spotter.backend.common.enums.InvitationStatus;
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
	name = "meetup_invitations",
	uniqueConstraints = @UniqueConstraint(name = "uk_meetup_invitations_meetup_user", columnNames = {"meetup_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetupInvitations extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "invited_by", nullable = false)
	private User invitedBy;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "meetup_id", nullable = false)
	private Meetups meetup;

	@Convert(converter = InvitationStatusConverter.class)
	@Column(nullable = false)
	private InvitationStatus status = InvitationStatus.PENDING;

	@Column(name = "responded_at")
	private LocalDateTime respondedAt;
}
