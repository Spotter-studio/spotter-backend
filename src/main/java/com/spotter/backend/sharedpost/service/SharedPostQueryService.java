package com.spotter.backend.sharedpost.service;

import com.spotter.backend.common.enums.SharedPostStatus;
import com.spotter.backend.sharedpost.dto.SharedPostDTO;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
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
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		return sharedPostRepository.findByUser_IdAndStatus(user.getId(), SharedPostStatus.PENDING).stream()
			.map(sp -> new SharedPostDTO.Response(
				sp.getId(),
				user.getId(),
				sp.getSourceUrl(),
				sp.getSourceType(),
				sp.getStatus(),
				List.of(),
				sp.getCreatedAt()
			))
			.toList();
	}


}
