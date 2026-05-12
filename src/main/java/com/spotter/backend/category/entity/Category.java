package com.spotter.backend.category.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

	@Id
	private Integer id;

	@Column(nullable = false, length = 50)
	private String name;

	public static Category of(Integer id, String name) {
		Category category = new Category();
		category.id = id;
		category.name = name;
		return category;
	}
}
