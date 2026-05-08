package com.spotter.backend.common.enums;

public enum SharedPostStatus implements CodeEnum {
	RAW(0),
	PENDING(1),
	CONFIRMED(2);

	private final int code;

	SharedPostStatus(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}
}
