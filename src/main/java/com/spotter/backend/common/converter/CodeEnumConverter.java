package com.spotter.backend.common.converter;

import com.spotter.backend.common.enums.CodeEnum;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;

public abstract class CodeEnumConverter<E extends Enum<E> & CodeEnum> implements AttributeConverter<E, Integer> {

	private final Class<E> enumClass;

	protected CodeEnumConverter(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public Integer convertToDatabaseColumn(E attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public E convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}

		return Arrays.stream(enumClass.getEnumConstants())
			.filter(value -> value.getCode() == dbData)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unknown " + enumClass.getSimpleName() + " code: " + dbData));
	}
}
