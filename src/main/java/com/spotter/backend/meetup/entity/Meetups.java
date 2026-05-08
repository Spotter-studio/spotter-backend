package com.spotter.backend.meetup.entity;

import com.spotter.backend.common.converter.MeetupStatusConverter;
import com.spotter.backend.common.converter.MeetupVisibilityConverter;
import com.spotter.backend.common.entity.BaseEntity;
import com.spotter.backend.common.enums.MeetupStatus;
import com.spotter.backend.common.enums.MeetupVisibility;
import com.spotter.backend.location.entity.Location;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meetups extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "host_id", nullable = false)
	private User host;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "location_id", nullable = false)
	private Location location;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(columnDefinition = "text")
	private String description;

	@Column(name = "meetup_at", nullable = false)
	private LocalDateTime meetupAt;

	@Column(name = "max_participants", nullable = false)
	private Integer maxParticipants = 2;

	@Convert(converter = MeetupStatusConverter.class)
	@Column(nullable = false)
	private MeetupStatus status = MeetupStatus.RECRUITING;

	@Convert(converter = MeetupVisibilityConverter.class)
	@Column(nullable = false)
	private MeetupVisibility visibility = MeetupVisibility.PUBLIC;
}
