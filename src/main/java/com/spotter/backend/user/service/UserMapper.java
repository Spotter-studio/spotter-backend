package com.spotter.backend.user.service;

import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.entity.User;

public final class UserMapper {

	private UserMapper() {
	}

	public static UserDTO.Response toResponse(User user) {
		return new UserDTO.Response(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getPreferenceDoc(),
			user.getCreatedAt()
		);
	}
}
