package com.spotter.backend.scrap.entity;

import com.spotter.backend.common.converter.SourceTypeConverter;
import com.spotter.backend.common.enums.SourceType;
import com.spotter.backend.location.entity.Location;
import com.spotter.backend.user.entity.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
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
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
	name = "scrap",
	uniqueConstraints = @UniqueConstraint(name = "uk_scrap_user_location", columnNames = {"user_id", "location_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "location_id", nullable = false)
	private Location location;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "scrap_source_url", joinColumns = @JoinColumn(name = "scrap_id"))
	@Column(name = "source_url", length = 1000, nullable = false)
	private List<String> sourceUrls = new ArrayList<>();

	@Convert(converter = SourceTypeConverter.class)
	@Column(name = "source_type", length = 20)
	private SourceType sourceType = SourceType.INSTAGRAM;

	@Column(name = "visit_count", nullable = false)
	private Integer visitCount = 0;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public Scrap(User user, Location location, String sourceUrl, SourceType sourceType) {
		this.user = user;
		this.location = location;
		if (sourceUrl != null) {
			this.sourceUrls.add(sourceUrl);
		}
		this.sourceType = sourceType;
	}

	public void addSourceUrl(String sourceUrl) {
		this.sourceUrls.add(sourceUrl);
	}
}
