package com.spotter.backend.category.service;

import com.spotter.backend.category.dto.CategoryDTO;
import com.spotter.backend.category.entity.Category;
import com.spotter.backend.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryQueryServiceTest {

	private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
	private final CategoryQueryService categoryQueryService = new CategoryQueryService(categoryRepository);

	@Test
	void listReturnsAllCategories() {
		Category c1 = makeCategory(1, "카페");
		Category c2 = makeCategory(2, "레스토랑");
		when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

		List<CategoryDTO.Response> result = categoryQueryService.list();

		assertThat(result).hasSize(2);
		assertThat(result.get(0).id()).isEqualTo(1);
		assertThat(result.get(0).name()).isEqualTo("카페");
		assertThat(result.get(1).id()).isEqualTo(2);
		assertThat(result.get(1).name()).isEqualTo("레스토랑");
	}

	@Test
	void listReturnsEmptyWhenNoCategoriesExist() {
		when(categoryRepository.findAll()).thenReturn(List.of());

		List<CategoryDTO.Response> result = categoryQueryService.list();

		assertThat(result).isEmpty();
	}

	private Category makeCategory(int id, String name) {
		return Category.of(id, name);
	}
}
