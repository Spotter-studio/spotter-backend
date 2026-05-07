package com.spotter.backend.common.enums;

import java.util.Arrays;

public enum SourceType {
	INSTAGRAM("instagram");

	private final String value;

	SourceType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static SourceType fromValue(String value) {
		return Arrays.stream(values())
			.filter(sourceType -> sourceType.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown source type: " + value));
	}
}
