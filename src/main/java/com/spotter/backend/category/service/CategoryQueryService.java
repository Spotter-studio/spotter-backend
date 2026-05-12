package com.spotter.backend.category.service;

import com.spotter.backend.category.dto.CategoryDTO;
import com.spotter.backend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryService {

	private final CategoryRepository categoryRepository;

	public List<CategoryDTO.Response> list() {
		return categoryRepository.findAll().stream()
			.map(CategoryMapper::toResponse)
			.toList();
	}
}
