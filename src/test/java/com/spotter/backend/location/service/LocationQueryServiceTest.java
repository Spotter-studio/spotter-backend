package com.spotter.backend.location.service;

import com.spotter.backend.category.entity.Category;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.location.entity.Location;
import com.spotter.backend.location.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LocationQueryServiceTest {

	private final LocationRepository locationRepository = mock(LocationRepository.class);
	private final LocationQueryService locationQueryService = new LocationQueryService(locationRepository);

	@Test
	void searchReturnsEmptyListWhenQueryIsBlank() {
		assertThat(locationQueryService.search("  ")).isEmpty();

		verifyNoInteractions(locationRepository);
	}

	@Test
	void searchReturnsMappedLocations() {
		Location location = buildLocation(1L, "카페 루트", "서울시 강남구", 3, "카페");
		when(locationRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase("카페", "카페"))
			.thenReturn(List.of(location));

		List<LocationDTO.Response> responses = locationQueryService.search("  카페  ");

		verify(locationRepository).findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase("카페", "카페");
		assertThat(responses).hasSize(1);
		LocationDTO.Response response = responses.get(0);
		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.name()).isEqualTo("카페 루트");
		assertThat(response.categoryName()).isEqualTo("카페");
		assertThat(response.totalScrapCount()).isEqualTo(3);
	}

	@Test
	void getThrowsWhenLocationMissing() {
		when(locationRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> locationQueryService.get(99L))
			.isInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.LOCATION_NOT_FOUND);
	}

	@Test
	void getReturnsLocationResponse() {
		Location location = buildLocation(2L, "맛집", "서울시 마포구", 1, "식당");
		when(locationRepository.findById(2L)).thenReturn(Optional.of(location));

		LocationDTO.Response response = locationQueryService.get(2L);

		assertThat(response.id()).isEqualTo(2L);
		assertThat(response.address()).isEqualTo("서울시 마포구");
		assertThat(response.categoryId()).isEqualTo(1);
		assertThat(response.categoryName()).isEqualTo("식당");
		assertThat(response.naverPlaceId()).isEqualTo("naver-2");
		assertThat(response.createdAt()).isEqualTo(LocalDateTime.of(2024, 2, 1, 12, 0));
	}

	private Location buildLocation(Long id, String name, String address, int scrapCount, String categoryName) {
		Category category = buildCategory(categoryName);

		Location location = new Location("naver-" + id, name, address,
			new BigDecimal("37.501"), new BigDecimal("127.001"), category);
		ReflectionTestUtils.setField(location, "id", id);
		ReflectionTestUtils.setField(location, "totalScrapCount", scrapCount);
		ReflectionTestUtils.setField(location, "createdAt", LocalDateTime.of(2024, 2, 1, 12, 0));
		ReflectionTestUtils.setField(location, "updatedAt", LocalDateTime.of(2024, 2, 2, 12, 0));
		return location;
	}

	private Category buildCategory(String categoryName) {
		try {
			Constructor<Category> constructor = Category.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			Category category = constructor.newInstance();
			ReflectionTestUtils.setField(category, "id", 1);
			ReflectionTestUtils.setField(category, "name", categoryName);
			return category;
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Failed to create category for tests", e);
		}
	}
}
