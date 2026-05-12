package com.spotter.backend.friendship.controller;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.friendship.dto.FriendshipDTO;
import com.spotter.backend.friendship.service.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

	private final FriendshipService friendshipService;

	// 친구 요청 전송. 자기 자신·이미 존재하는 요청은 불가
	@PostMapping("/requests")
	public ResponseEntity<ApiResponse<FriendshipDTO.Response>> sendRequest(
		@AuthenticationPrincipal Long userId,
		@RequestBody @Valid FriendshipDTO.CreateRequest request
	) {
		FriendshipDTO.Response response = friendshipService.sendRequest(userId, request.friendId());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(response));
	}

	// 수락된 친구 목록 조회. 내가 요청하거나 받은 것 모두 포함
	@GetMapping
	public ResponseEntity<ApiResponse<List<FriendshipDTO.FriendResponse>>> getFriends(
		@AuthenticationPrincipal Long userId
	) {
		List<FriendshipDTO.FriendResponse> responses = friendshipService.getFriends(userId);
		return ResponseEntity.ok(ApiResponse.onSuccess(responses));
	}

	// 나에게 온 PENDING 상태의 친구 요청 목록 조회
	@GetMapping("/requests/incoming")
	public ResponseEntity<ApiResponse<List<FriendshipDTO.Response>>> getIncomingRequests(
		@AuthenticationPrincipal Long userId
	) {
		List<FriendshipDTO.Response> responses = friendshipService.getIncomingRequests(userId);
		return ResponseEntity.ok(ApiResponse.onSuccess(responses));
	}

	// 친구 요청 수락. 요청을 받은 사람만 가능. 이미 처리된 요청은 400 반환
	@PostMapping("/requests/{requestId}/accept")
	public ResponseEntity<ApiResponse<FriendshipDTO.Response>> acceptRequest(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long requestId
	) {
		FriendshipDTO.Response response = friendshipService.acceptRequest(userId, requestId);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}

	// 친구 요청 거절. 요청을 받은 사람만 가능. 이미 처리된 요청은 400 반환
	@PostMapping("/requests/{requestId}/reject")
	public ResponseEntity<ApiResponse<FriendshipDTO.Response>> rejectRequest(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long requestId
	) {
		FriendshipDTO.Response response = friendshipService.rejectRequest(userId, requestId);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}
}
