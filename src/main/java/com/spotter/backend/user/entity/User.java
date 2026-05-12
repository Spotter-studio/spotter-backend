package com.spotter.backend.user.entity;

import com.spotter.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Column(name = "preference_doc", nullable = false, length = 2000)
	private String preferenceDoc;

	private User(String name, String email, String passwordHash) {
		this.name = name;
		this.email = email;
		this.passwordHash = passwordHash;
		this.preferenceDoc = "";
	}

	public static User create(String name, String email, String passwordHash) {
		return new User(name, email, passwordHash);
	}

	public void updateProfile(String name, String email) {
		if (name != null) {
			this.name = name;
		}
		if (email != null) {
			this.email = email;
		}
	}

	public void updatePassword(String passwordHash) {
		if (passwordHash != null) {
			this.passwordHash = passwordHash;
		}
	}
}
