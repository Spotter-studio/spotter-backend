package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.MeetupVisibility;
import jakarta.persistence.Converter;

@Converter
public class MeetupVisibilityConverter extends CodeEnumConverter<MeetupVisibility> {

	public MeetupVisibilityConverter() {
		super(MeetupVisibility.class);
	}
}
