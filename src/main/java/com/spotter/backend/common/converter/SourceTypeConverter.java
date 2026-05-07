package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.SourceType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SourceTypeConverter implements AttributeConverter<SourceType, String> {

	@Override
	public String convertToDatabaseColumn(SourceType attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public SourceType convertToEntityAttribute(String dbData) {
		return dbData == null ? null : SourceType.fromValue(dbData);
	}
}
