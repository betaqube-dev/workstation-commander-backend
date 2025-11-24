package dev.betaqube.wc.user.dto;

import dev.betaqube.wc.user.AppUser;

public class AppUserDto {

	private Long id;
	private String name;
	private String email;
	private String role;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	public static AppUserDto fromEntity(AppUser user) {
		AppUserDto dto = new AppUserDto();
		dto.id = user.getId();
		dto.name = user.getName();
		dto.email = user.getEmail();
		dto.role = user.getRole();
		return dto;
	}
}
