package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.InvitationStatus;
import jakarta.persistence.Converter;

@Converter
public class InvitationStatusConverter extends CodeEnumConverter<InvitationStatus> {

	public InvitationStatusConverter() {
		super(InvitationStatus.class);
	}
}
