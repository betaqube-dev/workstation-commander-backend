package dev.betaqube.wc.user.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequest {

	@NotBlank
	private String currentPassword;

	@NotBlank
	private String newPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
