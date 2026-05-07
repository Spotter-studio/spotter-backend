package com.spotter.backend.common.enums;

public enum InvitationStatus implements CodeEnum {
	PENDING(0),
	ACCEPTED(1),
	REJECTED(2);

	private final int code;

	InvitationStatus(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}
}
