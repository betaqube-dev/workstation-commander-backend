package dev.betaqube.wc.apikey;

import dev.betaqube.wc.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "api_keys")
public class ApiKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "key_value", nullable = false, unique = true, length = 128)
	private String keyValue;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime lastUsedAt;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private AppUser user;

	protected ApiKey() {
	}

	public ApiKey(String keyValue, LocalDateTime createdAt, AppUser user) {
		this.keyValue = keyValue;
		this.createdAt = createdAt;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastUsedAt() {
		return lastUsedAt;
	}

	public void setLastUsedAt(LocalDateTime lastUsedAt) {
		this.lastUsedAt = lastUsedAt;
	}

	public AppUser getUser() {
		return user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApiKey apiKey)) return false;
		return Objects.equals(id, apiKey.id) && Objects.equals(keyValue, apiKey.keyValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, keyValue);
	}
}
