package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.FriendshipStatus;
import jakarta.persistence.Converter;

@Converter
public class FriendshipStatusConverter extends CodeEnumConverter<FriendshipStatus> {

	public FriendshipStatusConverter() {
		super(FriendshipStatus.class);
	}
}
