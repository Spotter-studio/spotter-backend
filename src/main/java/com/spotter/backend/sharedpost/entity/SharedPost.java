package com.spotter.backend.sharedpost.entity;

import com.spotter.backend.common.converter.SourceTypeConverter;
import com.spotter.backend.common.entity.BaseTimeEntity;
import com.spotter.backend.common.enums.SourceType;
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

@Entity
@Table(name = "staged_data")
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

}
