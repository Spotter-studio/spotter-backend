package com.spotter.backend.friendship.service;

import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.friendship.dto.FriendshipDTO;
import com.spotter.backend.friendship.entity.Friendship;
import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceTest {

	@Mock
	private FriendshipRepository friendshipRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private FriendshipService friendshipService;

	// getId()·getName() 모두 lenient 처리 — 테스트별로 호출 여부가 다르기 때문
	private User mockUser(Long id, String name) {
		User user = mock(User.class);
		lenient().when(user.getId()).thenReturn(id);
		lenient().when(user.getName()).thenReturn(name);
		return user;
	}

	private Friendship pendingFriendship(Long id, User user, User friend) {
		Friendship friendship = Friendship.of(user, friend);
		ReflectionTestUtils.setField(friendship, "id", id);
		return friendship;
	}

	private Friendship acceptedFriendship(Long id, User user, User friend) {
		Friendship friendship = pendingFriendship(id, user, friend);
		friendship.accept();
		return friendship;
	}

	@Nested
	@DisplayName("sendRequest")
	class SendRequest {

		@Test
		@DisplayName("정상적으로 친구 요청을 보낸다")
		void success() {
			User user = mockUser(1L, "Alice");
			User friend = mockUser(2L, "Bob");
			when(userRepository.findById(1L)).thenReturn(Optional.of(user));
			when(userRepository.findById(2L)).thenReturn(Optional.of(friend));
			when(friendshipRepository.existsByUser_IdAndFriend_Id(1L, 2L)).thenReturn(false);
			when(friendshipRepository.existsByUser_IdAndFriend_Id(2L, 1L)).thenReturn(false);
			Friendship saved = pendingFriendship(1L, user, friend);
			when(friendshipRepository.save(any())).thenReturn(saved);

			FriendshipDTO.Response response = friendshipService.sendRequest(1L, 2L);

			assertThat(response.status()).isEqualTo(FriendshipStatus.PENDING);
			assertThat(response.userId()).isEqualTo(1L);
			assertThat(response.friendId()).isEqualTo(2L);
		}

		@Test
		@DisplayName("자기 자신에게 요청하면 SELF_FRIEND_REQUEST 예외가 발생한다")
		void selfRequest() {
			assertThatThrownBy(() -> friendshipService.sendRequest(1L, 1L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.SELF_FRIEND_REQUEST);
		}

		@Test
		@DisplayName("존재하지 않는 유저에게 요청하면 USER_NOT_FOUND 예외가 발생한다")
		void friendNotFound() {
			// mockUser는 when() 밖에서 미리 생성 (중첩 when() 방지)
			User alice = mockUser(1L, "Alice");
			when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
			when(userRepository.findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> friendshipService.sendRequest(1L, 99L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.USER_NOT_FOUND);
		}

		@Test
		@DisplayName("이미 요청이 존재하면 ALREADY_FRIEND 예외가 발생한다")
		void alreadyExists() {
			// mockUser는 when() 밖에서 미리 생성 (중첩 when() 방지)
			User alice = mockUser(1L, "Alice");
			User bob = mockUser(2L, "Bob");
			when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
			when(userRepository.findById(2L)).thenReturn(Optional.of(bob));
			when(friendshipRepository.existsByUser_IdAndFriend_Id(1L, 2L)).thenReturn(true);

			assertThatThrownBy(() -> friendshipService.sendRequest(1L, 2L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.ALREADY_FRIEND);
		}

		@Test
		@DisplayName("반대 방향 요청이 이미 있어도 ALREADY_FRIEND 예외가 발생한다")
		void alreadyExistsReverse() {
			User alice = mockUser(1L, "Alice");
			User bob = mockUser(2L, "Bob");
			when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
			when(userRepository.findById(2L)).thenReturn(Optional.of(bob));
			when(friendshipRepository.existsByUser_IdAndFriend_Id(1L, 2L)).thenReturn(false);
			when(friendshipRepository.existsByUser_IdAndFriend_Id(2L, 1L)).thenReturn(true);

			assertThatThrownBy(() -> friendshipService.sendRequest(1L, 2L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.ALREADY_FRIEND);
		}
	}

	@Nested
	@DisplayName("getIncomingRequests")
	class GetIncomingRequests {

		@Test
		@DisplayName("받은 PENDING 요청 목록을 반환한다")
		void success() {
			User requester = mockUser(1L, "Alice");
			User me = mockUser(2L, "Bob");
			Friendship f = pendingFriendship(10L, requester, me);
			when(friendshipRepository.findAllByFriend_IdAndStatus(2L, FriendshipStatus.PENDING))
				.thenReturn(List.of(f));

			List<FriendshipDTO.Response> result = friendshipService.getIncomingRequests(2L);

			assertThat(result).hasSize(1);
			assertThat(result.get(0).status()).isEqualTo(FriendshipStatus.PENDING);
			assertThat(result.get(0).userId()).isEqualTo(1L);
		}
	}

	@Nested
	@DisplayName("acceptRequest")
	class AcceptRequest {

		@Test
		@DisplayName("정상적으로 친구 요청을 수락한다")
		void success() {
			User requester = mockUser(1L, "Alice");
			User me = mockUser(2L, "Bob");
			Friendship f = pendingFriendship(10L, requester, me);
			when(friendshipRepository.findById(10L)).thenReturn(Optional.of(f));

			FriendshipDTO.Response response = friendshipService.acceptRequest(2L, 10L);

			assertThat(response.status()).isEqualTo(FriendshipStatus.ACCEPTED);
			assertThat(response.acceptedAt()).isNotNull();
		}

		@Test
		@DisplayName("요청을 받은 사람이 아니면 FORBIDDEN 예외가 발생한다")
		void notReceiver() {
			User requester = mockUser(1L, "Alice");
			User receiver = mockUser(2L, "Bob");
			Friendship f = pendingFriendship(10L, requester, receiver);
			when(friendshipRepository.findById(10L)).thenReturn(Optional.of(f));

			assertThatThrownBy(() -> friendshipService.acceptRequest(3L, 10L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.FORBIDDEN);
		}

		@Test
		@DisplayName("존재하지 않는 요청이면 FRIENDSHIP_NOT_FOUND 예외가 발생한다")
		void notFound() {
			when(friendshipRepository.findById(99L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> friendshipService.acceptRequest(2L, 99L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.FRIENDSHIP_NOT_FOUND);
		}

		@Test
		@DisplayName("이미 처리된 요청이면 BAD_REQUEST 예외가 발생한다")
		void alreadyProcessed() {
			User requester = mockUser(1L, "Alice");
			User me = mockUser(2L, "Bob");
			Friendship f = acceptedFriendship(10L, requester, me);
			when(friendshipRepository.findById(10L)).thenReturn(Optional.of(f));

			assertThatThrownBy(() -> friendshipService.acceptRequest(2L, 10L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.BAD_REQUEST);
		}
	}

	@Nested
	@DisplayName("rejectRequest")
	class RejectRequest {

		@Test
		@DisplayName("정상적으로 친구 요청을 거절한다")
		void success() {
			User requester = mockUser(1L, "Alice");
			User me = mockUser(2L, "Bob");
			Friendship f = pendingFriendship(10L, requester, me);
			when(friendshipRepository.findById(10L)).thenReturn(Optional.of(f));

			FriendshipDTO.Response response = friendshipService.rejectRequest(2L, 10L);

			assertThat(response.status()).isEqualTo(FriendshipStatus.REJECTED);
		}

		@Test
		@DisplayName("요청을 받은 사람이 아니면 FORBIDDEN 예외가 발생한다")
		void notReceiver() {
			User requester = mockUser(1L, "Alice");
			User receiver = mockUser(2L, "Bob");
			Friendship f = pendingFriendship(10L, requester, receiver);
			when(friendshipRepository.findById(10L)).thenReturn(Optional.of(f));

			assertThatThrownBy(() -> friendshipService.rejectRequest(3L, 10L))
				.isInstanceOf(BusinessException.class)
				.extracting(e -> ((BusinessException) e).getErrorCode())
				.isEqualTo(ErrorCode.FORBIDDEN);
		}
	}

	@Nested
	@DisplayName("getFriends")
	class GetFriends {

		@Test
		@DisplayName("내가 요청한 친구 관계에서 상대방 정보를 반환한다")
		void iAmRequester() {
			User me = mockUser(1L, "Alice");
			User friend = mockUser(2L, "Bob");
			Friendship f = acceptedFriendship(10L, me, friend);
			when(friendshipRepository.findAllAcceptedByUserId(1L, FriendshipStatus.ACCEPTED))
				.thenReturn(List.of(f));

			List<FriendshipDTO.FriendResponse> result = friendshipService.getFriends(1L);

			assertThat(result).hasSize(1);
			assertThat(result.get(0).friendId()).isEqualTo(2L);
			assertThat(result.get(0).friendName()).isEqualTo("Bob");
		}

		@Test
		@DisplayName("내가 요청을 받은 친구 관계에서 상대방 정보를 반환한다")
		void iAmReceiver() {
			User requester = mockUser(1L, "Alice");
			User me = mockUser(2L, "Bob");
			Friendship f = acceptedFriendship(10L, requester, me);
			when(friendshipRepository.findAllAcceptedByUserId(2L, FriendshipStatus.ACCEPTED))
				.thenReturn(List.of(f));

			List<FriendshipDTO.FriendResponse> result = friendshipService.getFriends(2L);

			assertThat(result).hasSize(1);
			assertThat(result.get(0).friendId()).isEqualTo(1L);
			assertThat(result.get(0).friendName()).isEqualTo("Alice");
		}
	}
}
