package com.spotter.backend.user.service;

import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserQueryService userQueryService;
	private final UserDeletionService userDeletionService;

	public UserDTO.Response signup(UserDTO.CreateRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
		}

		User user = User.create(
			request.name(),
			request.email(),
			passwordEncoder.encode(request.password())
		);
		return UserMapper.toResponse(userRepository.save(user));
	}

	public UserDTO.Response updateMe(Long userId, UserDTO.UpdateRequest request) {
		User user = userQueryService.findById(userId);

		String nextEmail = normalize(request.email());
		if (nextEmail != null && !nextEmail.equals(user.getEmail()) && userRepository.existsByEmail(nextEmail)) {
			throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
		}

		user.updateProfile(normalize(request.name()), nextEmail);
		user.updatePassword(encodePassword(request.password()));

		return UserMapper.toResponse(user);
	}

	public void deleteMe(Long userId) {
		User user = userQueryService.findById(userId);
		userDeletionService.deleteUserRelations(user.getId());
		userRepository.delete(user);
	}

	private String encodePassword(String password) {
		return StringUtils.hasText(password) ? passwordEncoder.encode(password) : null;
	}

	private String normalize(String value) {
		return StringUtils.hasText(value) ? value.trim() : null;
	}
}
