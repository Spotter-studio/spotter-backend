package com.spotter.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class UserDTO {

	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S+$";
	private static final String PASSWORD_MESSAGE = "Password must contain at least one lowercase letter, one uppercase letter, one number, one special character, and no whitespace.";

	private UserDTO() {
	}

	public record CreateRequest(
		@NotBlank
		@Size(max = 50)
		String name,

		@NotBlank
		@Email
		@Size(max = 100)
		String email,

		@NotBlank
		@Size(min = 8, max = 100)
		@Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
		String password
	) {
	}

	public record UpdateRequest(
		@Size(max = 50)
		String name,

		@Email
		@Size(max = 100)
		String email,

		@Size(min = 8, max = 100)
		@Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_MESSAGE)
		String password
	) {
	}

	public record LoginRequest(
		@NotBlank
		@Email
		@Size(max = 100)
		String email,

		@NotBlank
		@Size(min = 8, max = 100)
		String password
	) {
	}

	public record LoginResponse(
		String accessToken,
		String tokenType,
		long expiresInSeconds
	) {
	}

	public record Response(
		Long id,
		String name,
		String email,
		String preferenceDoc,
		LocalDateTime createdAt
	) {
	}
}
