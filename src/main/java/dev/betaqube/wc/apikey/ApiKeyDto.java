package dev.betaqube.wc.apikey;

import java.time.LocalDateTime;

public class ApiKeyDto {

	private Long id;
	private String keyValue;
	private LocalDateTime createdAt;
	private LocalDateTime lastUsedAt;

	public Long getId() {
		return id;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getLastUsedAt() {
		return lastUsedAt;
	}

	public static ApiKeyDto fromEntity(ApiKey apiKey) {
		ApiKeyDto dto = new ApiKeyDto();
		dto.id = apiKey.getId();
		dto.keyValue = apiKey.getKeyValue();
		dto.createdAt = apiKey.getCreatedAt();
		dto.lastUsedAt = apiKey.getLastUsedAt();
		return dto;
	}
}
