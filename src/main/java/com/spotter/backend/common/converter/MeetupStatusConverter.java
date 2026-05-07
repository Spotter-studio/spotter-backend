package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.MeetupStatus;
import jakarta.persistence.Converter;

@Converter
public class MeetupStatusConverter extends CodeEnumConverter<MeetupStatus> {

	public MeetupStatusConverter() {
		super(MeetupStatus.class);
	}
}
