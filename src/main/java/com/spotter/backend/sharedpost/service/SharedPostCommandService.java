package com.spotter.backend.sharedpost.service;

import com.spotter.backend.category.entity.Category;
import com.spotter.backend.category.repository.CategoryRepository;
import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.location.entity.Location;
import com.spotter.backend.location.repository.LocationRepository;
import com.spotter.backend.scrap.entity.Scrap;
import com.spotter.backend.scrap.repository.ScrapRepository;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import com.spotter.backend.sharedpost.entity.SharedPost;
import com.spotter.backend.sharedpost.repository.SharedPostRepository;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

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
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		SharedPost sharedPost = sharedPostRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.SHARED_POST_NOT_FOUND));

		if (!sharedPost.getUser().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCode.SHARED_POST_FORBIDDEN);
		}

		List<Location> resolvedLocations = new ArrayList<>();

		for (SharedPostDTO.LocationInput input : request.locations()) {
			Optional<Location> existing = findExistingLocation(input);
			if (existing.isPresent()) {
				Location location = existing.get();
				resolvedLocations.add(location);
				createScrapIfNeeded(user, location, sharedPost);
			} else {
				resolvedLocations.add(createLocation(input));
			}
		}

		sharedPost.confirm();

		return new SharedPostDTO.Response(
			sharedPost.getId(),
			user.getId(),
			sharedPost.getSourceUrl(),
			sharedPost.getSourceType(),
			sharedPost.getStatus(),
			resolvedLocations.stream().map(this::toLocationResponse).toList(),
			sharedPost.getCreatedAt()
		);
	}

    //장소 정보 검색 1. spotter 전용 DB, 2.네이버 검색 api 3. 모두 없으면 empty
	private Optional<Location> findExistingLocation(SharedPostDTO.LocationInput input) {
		if (input.id() != null) {
			return locationRepository.findById(input.id());
		}
		if (input.naverPlaceId() != null) {
			return locationRepository.findByNaverPlaceId(input.naverPlaceId());
		}
		return Optional.empty();
	}
    // 장소 검증 시 scrap 생성 1. 이미 저장된 장소일 시 url 추가 2. 없으면 새로운 scrap 생성 3.게시물 정보 없이 사용자가 장소만 저장할 시-> 스트랩 생성
	private void createScrapIfNeeded(User user, Location location, SharedPost sharedPost) {
		String sourceUrl = sharedPost.getSourceUrl();
		if (sourceUrl != null) {
			scrapRepository.findByUser_IdAndLocation_Id(user.getId(), location.getId())
				.ifPresentOrElse(
					scrap -> scrap.addSourceUrl(sourceUrl),
					() -> scrapRepository.save(new Scrap(user, location, sourceUrl, sharedPost.getSourceType()))
				);
		} else if (!scrapRepository.existsByUser_IdAndLocation_Id(user.getId(), location.getId())) {
			scrapRepository.save(new Scrap(user, location, null, sharedPost.getSourceType()));
		}
	}

	public void delete(String email, Long postId) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		SharedPost sharedPost = sharedPostRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ErrorCode.SHARED_POST_NOT_FOUND));

		if (!sharedPost.getUser().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCode.SHARED_POST_FORBIDDEN);
		}

		sharedPostRepository.delete(sharedPost);
	}

	private LocationDTO.Response toLocationResponse(Location loc) {
		return new LocationDTO.Response(
			loc.getId(),
			loc.getLatitude(),
			loc.getLongitude(),
			loc.getCategory().getId(),
			loc.getCategory().getName(),
			loc.getNaverPlaceId(),
			loc.getName(),
			loc.getAddress(),
			loc.getTotalScrapCount(),
			loc.getCreatedAt(),
			loc.getUpdatedAt()
		);
	}
//사용자 sharedPost 확정 시 장소 저장(기존 장소 정보가 존재하지 않는 경우)
	private Location createLocation(SharedPostDTO.LocationInput input) {
		if (input.name() == null || input.latitude() == null || input.longitude() == null || input.categoryId() == null) {
			throw new BusinessException(ErrorCode.LOCATION_INVALID_INPUT);
		}

		Category category = categoryRepository.findById(input.categoryId())
			.orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

		return locationRepository.save(
			new Location(input.naverPlaceId(), input.name(), input.address(), input.latitude(), input.longitude(), category)
		);
	}
}
