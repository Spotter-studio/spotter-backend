package com.spotter.backend.user.controller;

import com.spotter.backend.auth.AuthenticatedUser;
import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.service.UserAuthService;
import com.spotter.backend.user.service.UserCommandService;
import com.spotter.backend.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "유저 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserCommandService userCommandService;
	private final UserQueryService userQueryService;
	private final UserAuthService userAuthService;

	@Operation(summary = "회원가입")
	@PostMapping("/signup")
	public ResponseEntity<UserDTO.Response> signup(@Valid @RequestBody UserDTO.CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userCommandService.signup(request));
	}

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public ResponseEntity<UserDTO.LoginResponse> login(@Valid @RequestBody UserDTO.LoginRequest request) {
		return ResponseEntity.ok(userAuthService.login(request));
	}

	@Operation(summary = "내 정보 조회")
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/me")
	public ResponseEntity<UserDTO.Response> me(Authentication authentication) {
		return ResponseEntity.ok(userQueryService.me(AuthenticatedUser.id(authentication)));
	}

	@Operation(summary = "내 정보 수정")
	@SecurityRequirement(name = "bearerAuth")
	@PatchMapping("/me")
	public ResponseEntity<UserDTO.Response> updateMe(
		Authentication authentication,
		@Valid @RequestBody UserDTO.UpdateRequest request
	) {
		return ResponseEntity.ok(userCommandService.updateMe(AuthenticatedUser.id(authentication), request));
	}

	@Operation(summary = "회원 탈퇴")
	@SecurityRequirement(name = "bearerAuth")
	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteMe(Authentication authentication) {
		userCommandService.deleteMe(AuthenticatedUser.id(authentication));
		return ResponseEntity.noContent().build();
	}
}
