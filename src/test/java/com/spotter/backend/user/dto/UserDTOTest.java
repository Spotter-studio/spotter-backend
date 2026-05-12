package com.spotter.backend.user.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserDTOTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeAll
	static void setUpValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void closeValidator() {
		validatorFactory.close();
	}

	@Test
	void createRequestAcceptsPasswordWithRequiredCharacterClasses() {
		UserDTO.CreateRequest request = new UserDTO.CreateRequest("user", "user@example.com", "pAssword1!");

		Set<ConstraintViolation<UserDTO.CreateRequest>> violations = validator.validate(request);

		assertThat(violations).isEmpty();
	}

	@Test
	void createRequestRejectsPasswordWithoutLowercaseUppercaseNumberSpecialCharacterOrWithWhitespace() {
		assertPasswordViolation("PASSWORD1!");
		assertPasswordViolation("password1!");
		assertPasswordViolation("Password!");
		assertPasswordViolation("Password1");
		assertPasswordViolation("Password 1!");
	}

	@Test
	void updateRequestAllowsNullPassword() {
		UserDTO.UpdateRequest request = new UserDTO.UpdateRequest(null, null, null);

		Set<ConstraintViolation<UserDTO.UpdateRequest>> violations = validator.validate(request);

		assertThat(violations).isEmpty();
	}

	private void assertPasswordViolation(String password) {
		UserDTO.CreateRequest request = new UserDTO.CreateRequest("user", "user@example.com", password);

		Set<ConstraintViolation<UserDTO.CreateRequest>> violations = validator.validate(request);

		assertThat(violations)
			.anyMatch(violation -> violation.getPropertyPath().toString().equals("password"));
	}
}
