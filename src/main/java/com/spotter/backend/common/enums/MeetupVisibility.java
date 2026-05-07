package com.spotter.backend.common.enums;

public enum MeetupVisibility implements CodeEnum {
	PUBLIC(0),
	FRIENDS_ONLY(1),
	PRIVATE(2);

	private final int code;

	MeetupVisibility(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}
}
