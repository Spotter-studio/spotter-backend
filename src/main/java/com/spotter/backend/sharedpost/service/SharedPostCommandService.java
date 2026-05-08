package com.spotter.backend.sharedpost.service;

import com.spotter.backend.category.entity.Category;
import com.spotter.backend.category.repository.CategoryRepository;
import com.spotter.backend.location.entity.Location;
import com.spotter.backend.location.repository.LocationRepository;
import com.spotter.backend.scrap.entity.Scrap;
import com.spotter.backend.scrap.repository.ScrapRepository;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import com.spotter.backend.sharedpost.entity.SharedPost;
import com.spotter.backend.sharedpost.repository.SharedPostRepository;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SharedPostCommandService {

	private final SharedPostRepository sharedPostRepository;
	private final UserRepository userRepository;
	private final LocationRepository locationRepository;
	private final CategoryRepository categoryRepository;
	private final ScrapRepository scrapRepository;

	// User 정보 가져오는 로직 구현 시 변경 예정
	public SharedPostDTO.Response create(String email, SharedPostDTO.CreateRequest request) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

		SharedPost sharedPost = new SharedPost(
			user,
			request.sourceUrl(),
			request.sourceType(),
			request.ocrText(),
			request.imageUrls()
		);

		SharedPost saved = sharedPostRepository.save(sharedPost);

		return new SharedPostDTO.Response(
			saved.getId(),
			user.getId(),
			saved.getSourceUrl(),
			saved.getSourceType(),
			saved.getStatus(),
			List.of(),
			saved.getCreatedAt()
		);
	}

	public SharedPostDTO.Response confirm(String email, Long postId, SharedPostDTO.ConfirmRequest request) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

		SharedPost sharedPost = sharedPostRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("SharedPost not found: " + postId));

		if (!sharedPost.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException("SharedPost does not belong to user");
		}

		List<Location> confirmedLocations = new ArrayList<>();

		String sourceUrl = sharedPost.getSourceUrl();

		for (SharedPostDTO.LocationInput input : request.locations()) {
			Location location = resolveLocation(input);
			confirmedLocations.add(location);

			if (sourceUrl != null) {
				scrapRepository.findByUser_IdAndLocation_Id(user.getId(), location.getId())
					.ifPresentOrElse(
						scrap -> scrap.addSourceUrl(sourceUrl),
						() -> scrapRepository.save(new Scrap(user, location, sourceUrl, sharedPost.getSourceType()))
					);
			} else if (!scrapRepository.existsByUser_IdAndLocation_Id(user.getId(), location.getId())) {
				scrapRepository.save(new Scrap(user, location, sourceUrl, sharedPost.getSourceType()));
			}
		}

		sharedPost.confirm(confirmedLocations);

		return new SharedPostDTO.Response(
			sharedPost.getId(),
			user.getId(),
			sharedPost.getSourceUrl(),
			sharedPost.getSourceType(),
			sharedPost.getStatus(),
			confirmedLocations.stream()
				.map(loc -> new SharedPostDTO.LocationSummary(
					loc.getId(),
					loc.getName(),
					loc.getAddress(),
					loc.getLatitude(),
					loc.getLongitude(),
					loc.getCategory().getId(),
					loc.getCategory().getName()
				))
				.toList(),
			sharedPost.getCreatedAt()
		);
	}

	public void delete(String email, Long postId) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

		SharedPost sharedPost = sharedPostRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("SharedPost not found: " + postId));

		if (!sharedPost.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException("SharedPost does not belong to user");
		}

		sharedPostRepository.delete(sharedPost);
	}

	private Location resolveLocation(SharedPostDTO.LocationInput input) {
		if (input.id() != null) {
			return locationRepository.findById(input.id())
				.orElseThrow(() -> new IllegalArgumentException("Location not found: " + input.id()));
		}

		if (input.naverPlaceId() != null) {
			return locationRepository.findByNaverPlaceId(input.naverPlaceId())
				.orElseGet(() -> createLocation(input));
		}

		return createLocation(input);
	}

	private Location createLocation(SharedPostDTO.LocationInput input) {
		if (input.name() == null || input.latitude() == null || input.longitude() == null || input.categoryId() == null) {
			throw new IllegalArgumentException("name, latitude, longitude, categoryId are required for new locations");
		}

		Category category = categoryRepository.findById(input.categoryId())
			.orElseThrow(() -> new IllegalArgumentException("Category not found: " + input.categoryId()));

		return locationRepository.save(
			new Location(input.naverPlaceId(), input.name(), input.address(), input.latitude(), input.longitude(), category)
		);
	}
}
