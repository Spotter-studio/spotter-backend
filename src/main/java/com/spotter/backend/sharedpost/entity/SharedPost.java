package com.spotter.backend.sharedpost.entity;

import com.spotter.backend.common.converter.SharedPostStatusConverter;
import com.spotter.backend.common.converter.SourceTypeConverter;
import com.spotter.backend.common.entity.BaseTimeEntity;
import com.spotter.backend.common.enums.SharedPostStatus;
import com.spotter.backend.common.enums.SourceType;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
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
public class SharedPost extends BaseTimeEntity {

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

    //서버에서 LLM과 네이버 지도 검색 api로 장소 정보 추출이 완료되었을 때-> SharedPost의 상태를 Raw-> PENDING으로 변환(사용자 검증 전)
	public void toPending() {
		if (this.status != SharedPostStatus.RAW) {
			throw new BusinessException(ErrorCode.SHARED_POST_INVALID_STATUS);
		}
		this.status = SharedPostStatus.PENDING;
	}

	public void confirm() {
		if (this.status != SharedPostStatus.PENDING) {
			throw new BusinessException(ErrorCode.SHARED_POST_INVALID_STATUS);
		}
		this.status = SharedPostStatus.CONFIRMED;
	}
}
