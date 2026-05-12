package com.spotter.backend.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotter.backend.auth.JwtTokenProvider;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAuthServiceTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(new ObjectMapper(), "test-secret", 3600);
	private final UserAuthService userAuthService = new UserAuthService(userRepository, passwordEncoder, jwtTokenProvider);

	@Test
	void loginReturnsAccessToken() {
		User user = User.create("user", "user@example.com", passwordEncoder.encode("password123"));
		ReflectionTestUtils.setField(user, "id", 1L);
		when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

		UserDTO.LoginResponse response = userAuthService.login(
			new UserDTO.LoginRequest("user@example.com", "password123"));

		assertThat(response.tokenType()).isEqualTo("Bearer");
		assertThat(response.accessToken()).isNotBlank();
		assertThat(jwtTokenProvider.validateAndGetUserId(response.accessToken())).isEqualTo(1L);
	}

	@Test
	void loginFailsWithSameErrorForUnknownEmailAndWrongPassword() {
		when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
		User user = User.create("user", "user@example.com", passwordEncoder.encode("password123"));
		when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

		assertInvalidCredentials(new UserDTO.LoginRequest("missing@example.com", "password123"));
		assertInvalidCredentials(new UserDTO.LoginRequest("user@example.com", "wrong-password"));
	}

	private void assertInvalidCredentials(UserDTO.LoginRequest request) {
		assertThatThrownBy(() -> userAuthService.login(request))
			.isInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.INVALID_CREDENTIALS);
	}
}
