package com.spotter.backend.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotter.backend.user.dto.UserDTO;
import com.spotter.backend.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

	private static final String HMAC_ALGORITHM = "HmacSHA256";
	private static final String TOKEN_TYPE = "Bearer";

	private final ObjectMapper objectMapper;
	private final byte[] secretBytes;
	private final long expiresInSeconds;

	public JwtTokenProvider(
		ObjectMapper objectMapper,
		@Value("${spotter.jwt.secret}") String secret,
		@Value("${spotter.jwt.expires-in-seconds:3600}") long expiresInSeconds
	) {
		if (secret == null || secret.isBlank()) {
			throw new IllegalStateException("Required property 'spotter.jwt.secret' must be configured and non-blank");
		}

		this.objectMapper = objectMapper;
		this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
		this.expiresInSeconds = expiresInSeconds;
	}

	public UserDTO.LoginResponse createAccessToken(User user) {
		long now = Instant.now().getEpochSecond();
		Map<String, Object> header = new LinkedHashMap<>();
		header.put("alg", "HS256");
		header.put("typ", "JWT");

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("sub", user.getId().toString());
		payload.put("email", user.getEmail());
		payload.put("iat", now);
		payload.put("exp", now + expiresInSeconds);

		String token = encode(header) + "." + encode(payload);
		token = token + "." + sign(token);
		return new UserDTO.LoginResponse(token, TOKEN_TYPE, expiresInSeconds);
	}

	public Long validateAndGetUserId(String token) {
		try {
			String[] parts = token.split("\\.");
			if (parts.length != 3) {
				return null;
			}

			String unsignedToken = parts[0] + "." + parts[1];
			if (!constantTimeEquals(sign(unsignedToken), parts[2])) {
				return null;
			}

			Map<String, Object> payload = objectMapper.readValue(
				Base64.getUrlDecoder().decode(parts[1]),
				new TypeReference<>() {
				}
			);
			long expiration = ((Number) payload.get("exp")).longValue();
			if (expiration < Instant.now().getEpochSecond()) {
				return null;
			}

			String subject = (String) payload.get("sub");
			return subject == null || subject.isBlank() ? null : Long.valueOf(subject);
		} catch (RuntimeException | java.io.IOException e) {
			return null;
		}
	}

	private String encode(Map<String, Object> value) {
		try {
			return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(objectMapper.writeValueAsBytes(value));
		} catch (java.io.IOException e) {
			throw new IllegalStateException("Failed to encode JWT", e);
		}
	}

	private String sign(String value) {
		try {
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			mac.init(new SecretKeySpec(secretBytes, HMAC_ALGORITHM));
			return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to sign JWT", e);
		}
	}

	private boolean constantTimeEquals(String expected, String actual) {
		return expected.length() == actual.length()
			&& MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
	}
}
