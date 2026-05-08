package com.spotter.backend.sharedpost.entity;

import com.spotter.backend.common.converter.SharedPostStatusConverter;
import com.spotter.backend.common.converter.SourceTypeConverter;
import com.spotter.backend.common.enums.SharedPostStatus;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shared_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "source_url", length = 1000)
	private String sourceUrl;

	@Convert(converter = SourceTypeConverter.class)
	@Column(name = "source_type", length = 20)
	private SourceType sourceType = SourceType.INSTAGRAM;

	@Convert(converter = SharedPostStatusConverter.class)
	@Column(nullable = false)
	private SharedPostStatus status = SharedPostStatus.RAW;

	@Column(name = "ocr_text", columnDefinition = "text")
	private String ocrText;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "shared_post_image", joinColumns = @JoinColumn(name = "shared_post_id"))
	@Column(name = "image_url", length = 1000, nullable = false)
	private List<String> imageUrls = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "shared_post_location",
		joinColumns = @JoinColumn(name = "shared_post_id"),
		inverseJoinColumns = @JoinColumn(name = "location_id")
	)
	private List<Location> locations = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public SharedPost(User user, String sourceUrl, SourceType sourceType, String ocrText, List<String> imageUrls) {
		this.user = user;
		this.sourceUrl = sourceUrl;
		this.sourceType = sourceType;
		this.ocrText = ocrText;
		this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
	}

	public void confirm(List<Location> confirmedLocations) {
		if (this.status != SharedPostStatus.PENDING) {
			throw new IllegalStateException("SharedPost must be in PENDING status to confirm. Current status: " + this.status);
		}
		this.status = SharedPostStatus.CONFIRMED;
		this.locations.clear();
		this.locations.addAll(confirmedLocations);
	}
}
