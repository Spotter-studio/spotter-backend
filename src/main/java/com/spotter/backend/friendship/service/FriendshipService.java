package com.spotter.backend.friendship.service;

import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.friendship.dto.FriendshipDTO;
import com.spotter.backend.friendship.entity.Friendship;
import com.spotter.backend.friendship.repository.FriendshipRepository;
import com.spotter.backend.location.dto.LocationDTO;
import com.spotter.backend.scrap.repository.ScrapRepository;
import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipService {

	private final FriendshipRepository friendshipRepository;
	private final UserRepository userRepository;
	private final ScrapRepository scrapRepository;

	// 친구 요청 전송. 자기 자신·양방향 중복 요청 방지
	@Transactional
	public FriendshipDTO.Response sendRequest(Long userId, Long friendId) {
		if (userId.equals(friendId)) {
			throw new BusinessException(ErrorCode.SELF_FRIEND_REQUEST);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
		User friend = userRepository.findById(friendId)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		// 양방향 중복 체크
		if (friendshipRepository.existsBetween(userId, friendId)) {
			throw new BusinessException(ErrorCode.ALREADY_FRIEND);
		}

		Friendship friendship = friendshipRepository.save(Friendship.of(user, friend));
		return toResponse(friendship);
	}

	// 나에게 온 PENDING 상태의 친구 요청 목록 반환
	public List<FriendshipDTO.Response> getIncomingRequests(Long userId) {
		return friendshipRepository
			.findAllByFriend_IdAndStatus(userId, FriendshipStatus.PENDING)
			.stream()
			.map(this::toResponse)
			.toList();
	}

	// 수락된 친구 목록 반환. 내가 요청하거나 받은 것 모두 포함
	public List<FriendshipDTO.FriendResponse> getFriends(Long userId) {
		return friendshipRepository
			.findAllAcceptedByUserId(userId, FriendshipStatus.ACCEPTED)
			.stream()
			.map(f -> toFriendResponse(f, userId))
			.toList();
	}

	// 요청을 받은 사람(friend)만 수락 가능
	@Transactional
	public FriendshipDTO.Response acceptRequest(Long userId, Long requestId) {
		Friendship friendship = findPendingRequest(requestId);

		if (!friendship.getFriend().getId().equals(userId)) {
			throw new BusinessException(ErrorCode.FORBIDDEN);
		}

		friendship.accept();
		return toResponse(friendship);
	}

	// 요청을 받은 사람(friend)만 거절 가능
	@Transactional
	public FriendshipDTO.Response rejectRequest(Long userId, Long requestId) {
		Friendship friendship = findPendingRequest(requestId);

		if (!friendship.getFriend().getId().equals(userId)) {
			throw new BusinessException(ErrorCode.FORBIDDEN);
		}

		friendship.reject();
		return toResponse(friendship);
	}

	// 나와 친구 공통 장소 목록 반환
	public List<LocationDTO.Response> getCommonLocations(Long userId, Long friendId) {
		if (!friendshipRepository.existsBidirectional(userId, friendId, FriendshipStatus.ACCEPTED)) {
			throw new BusinessException(ErrorCode.NOT_FRIENDS);
		}

		return scrapRepository.findCommonLocations(userId, friendId)
			.stream()
			.map(location -> new LocationDTO.Response(
				location.getId(),
				location.getLatitude(),
				location.getLongitude(),
				location.getCategory().getId(),
				location.getCategory().getName(),
				location.getNaverPlaceId(),
				location.getName(),
				location.getAddress(),
				location.getTotalScrapCount(),
				location.getCreatedAt(),
				location.getUpdatedAt()
			))
			.toList();
	}

	// PENDING 상태인 요청만 수락/거절 가능
	private Friendship findPendingRequest(Long requestId) {
		Friendship friendship = friendshipRepository.findById(requestId)
			.orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

		if (friendship.getStatus() != FriendshipStatus.PENDING) {
			throw new BusinessException(ErrorCode.FRIEND_REQUEST_ALREADY_PROCESSED);
		}

		return friendship;
	}

	// 양방향 관계에서 상대방 유저 정보를 추출
	private FriendshipDTO.FriendResponse toFriendResponse(Friendship friendship, Long myId) {
		boolean iAmRequester = friendship.getUser().getId().equals(myId);
		Long friendId = iAmRequester ? friendship.getFriend().getId() : friendship.getUser().getId();
		String friendName = iAmRequester ? friendship.getFriend().getName() : friendship.getUser().getName();
		return new FriendshipDTO.FriendResponse(friendship.getId(), friendId, friendName, friendship.getAcceptedAt());
	}

	private FriendshipDTO.Response toResponse(Friendship friendship) {
		return new FriendshipDTO.Response(
			friendship.getId(),
			friendship.getUser().getId(),
			friendship.getFriend().getId(),
			friendship.getStatus(),
			friendship.getCreatedAt(),
			friendship.getAcceptedAt()
		);
	}
}
