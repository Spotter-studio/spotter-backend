package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.SharedPostStatus;
import jakarta.persistence.Converter;

@Converter
public class SharedPostStatusConverter extends CodeEnumConverter<SharedPostStatus> {

	public SharedPostStatusConverter() {
		super(SharedPostStatus.class);
	}
}
