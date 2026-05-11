package com.spotter.backend.user.service;

import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCommandServiceTest {

	private final UserRepository userRepository = mock(UserRepository.class);
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final UserQueryService userQueryService = new UserQueryService(userRepository);
	private final UserDeletionService userDeletionService = mock(UserDeletionService.class);
	private final UserCommandService userCommandService = new UserCommandService(
		userRepository,
		passwordEncoder,
		userQueryService,
		userDeletionService
	);

	@Test
	void signupHashesPasswordAndInitializesPreferenceDoc() {
		when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		userCommandService.signup(new UserDTO.CreateRequest("user", "user@example.com", "password123"));

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		User savedUser = captor.getValue();
		assertThat(savedUser.getPreferenceDoc()).isEmpty();
		assertThat(passwordEncoder.matches("password123", savedUser.getPasswordHash())).isTrue();
	}

	@Test
	void signupRejectsDuplicateEmail() {
		when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

		assertThatThrownBy(() -> userCommandService.signup(
			new UserDTO.CreateRequest("user", "user@example.com", "password123")))
			.isInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.DUPLICATE_EMAIL);
	}

	@Test
	void updateMeUpdatesOnlyRequestedFields() {
		User user = User.create("old", "old@example.com", passwordEncoder.encode("password123"));
		ReflectionTestUtils.setField(user, "id", 1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

		userCommandService.updateMe(1L, new UserDTO.UpdateRequest("new", "new@example.com", "newpassword123"));

		assertThat(user.getName()).isEqualTo("new");
		assertThat(user.getEmail()).isEqualTo("new@example.com");
		assertThat(passwordEncoder.matches("newpassword123", user.getPasswordHash())).isTrue();
		assertThat(user.getPreferenceDoc()).isEmpty();
	}

	@Test
	void updateMeRejectsDuplicateEmail() {
		User user = User.create("old", "old@example.com", passwordEncoder.encode("password123"));
		ReflectionTestUtils.setField(user, "id", 1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.existsByEmail("new@example.com")).thenReturn(true);

		assertThatThrownBy(() -> userCommandService.updateMe(
			1L,
			new UserDTO.UpdateRequest(null, "new@example.com", null)))
			.isInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(ErrorCode.DUPLICATE_EMAIL);
	}

	@Test
	void deleteMeDeletesRelationsBeforeUser() {
		User user = User.create("user", "user@example.com", passwordEncoder.encode("password123"));
		ReflectionTestUtils.setField(user, "id", 1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userCommandService.deleteMe(1L);

		InOrder inOrder = inOrder(userDeletionService, userRepository);
		inOrder.verify(userDeletionService).deleteUserRelations(1L);
		inOrder.verify(userRepository).delete(user);
	}
}
