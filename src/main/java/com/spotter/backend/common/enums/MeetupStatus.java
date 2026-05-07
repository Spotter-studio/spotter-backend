package com.spotter.backend.common.enums;

public enum MeetupStatus implements CodeEnum {
	RECRUITING(0),
	ENDED(1),
	CANCELLED(2);

	private final int code;

	MeetupStatus(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}
}
