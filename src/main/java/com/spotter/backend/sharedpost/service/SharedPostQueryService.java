package com.spotter.backend.sharedpost.service;

import com.spotter.backend.common.enums.SharedPostStatus;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import com.spotter.backend.sharedpost.repository.SharedPostRepository;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharedPostQueryService {

	private final SharedPostRepository sharedPostRepository;
	private final UserRepository userRepository;

	// User 정보 가져오는 로직 구현 시 변경 예정
	public List<SharedPostDTO.Response> getPending(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

		return sharedPostRepository.findByUser_IdAndStatus(user.getId(), SharedPostStatus.PENDING).stream()
			.map(sp -> new SharedPostDTO.Response(
				sp.getId(),
				user.getId(),
				sp.getSourceUrl(),
				sp.getSourceType(),
				sp.getStatus(),
				sp.getLocations().stream()
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
				sp.getCreatedAt()
			))
			.toList();
	}
}
