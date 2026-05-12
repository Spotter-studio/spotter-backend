package com.spotter.backend.user.controller.docs;

import com.spotter.backend.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Users", description = "유저 관련 API")
public interface UserControllerDocs {

	@Operation(summary = "회원가입")
	ResponseEntity<UserDTO.Response> signup(@Valid @RequestBody UserDTO.CreateRequest request);

	@Operation(summary = "로그인")
	ResponseEntity<UserDTO.LoginResponse> login(@Valid @RequestBody UserDTO.LoginRequest request);

	@Operation(summary = "내 정보 조회")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UserDTO.Response> me(Authentication authentication);

	@Operation(summary = "내 정보 수정")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<UserDTO.Response> updateMe(Authentication authentication, @Valid @RequestBody UserDTO.UpdateRequest request);

	@Operation(summary = "회원 탈퇴")
	@SecurityRequirement(name = "bearerAuth")
	ResponseEntity<Void> deleteMe(Authentication authentication);
}
