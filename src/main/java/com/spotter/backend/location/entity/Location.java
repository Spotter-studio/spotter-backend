package com.spotter.backend.location.entity;

import com.spotter.backend.category.entity.Category;
import com.spotter.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
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

import java.math.BigDecimal;

@Entity
@Table(name = "location")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, precision = 10, scale = 7)
	private BigDecimal latitude;

	@Column(nullable = false, precision = 10, scale = 7)
	private BigDecimal longitude;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "naver_place_id", length = 100)
	private String naverPlaceId;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(length = 500)
	private String address;

	@Column(name = "total_scrap_count", nullable = false)
	private Integer totalScrapCount = 0;

	public Location(String naverPlaceId, String name, String address, BigDecimal latitude, BigDecimal longitude, Category category) {
		this.naverPlaceId = naverPlaceId;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.category = category;

	}
}
