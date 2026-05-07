package com.spotter.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class UserDTO {

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
		String password
	) {
	}

	public record UpdateRequest(
		@Size(max = 50)
		String name,

		@Size(max = 2000)
		String preferenceDoc
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
