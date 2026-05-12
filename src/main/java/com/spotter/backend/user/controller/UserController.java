package com.spotter.backend.user.controller;

import com.spotter.backend.auth.AuthenticatedUser;
import com.spotter.backend.user.controller.docs.UserControllerDocs;
import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.service.UserAuthService;
import com.spotter.backend.user.service.UserCommandService;
import com.spotter.backend.user.service.UserQueryService;
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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

	private final UserCommandService userCommandService;
	private final UserQueryService userQueryService;
	private final UserAuthService userAuthService;

	@PostMapping("/signup")
	public ResponseEntity<UserDTO.Response> signup(@Valid @RequestBody UserDTO.CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userCommandService.signup(request));
	}

	@PostMapping("/login")
	public ResponseEntity<UserDTO.LoginResponse> login(@Valid @RequestBody UserDTO.LoginRequest request) {
		return ResponseEntity.ok(userAuthService.login(request));
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO.Response> me(Authentication authentication) {
		return ResponseEntity.ok(userQueryService.me(AuthenticatedUser.id(authentication)));
	}

	@PatchMapping("/me")
	public ResponseEntity<UserDTO.Response> updateMe(
		Authentication authentication,
		@Valid @RequestBody UserDTO.UpdateRequest request
	) {
		return ResponseEntity.ok(userCommandService.updateMe(AuthenticatedUser.id(authentication), request));
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteMe(Authentication authentication) {
		userCommandService.deleteMe(AuthenticatedUser.id(authentication));
		return ResponseEntity.noContent().build();
	}
}
