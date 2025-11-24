package dev.betaqube.wc.apikey;

import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class ApiKeyService {

	private static final int KEY_SIZE_BYTES = 32;

	private final ApiKeyRepository apiKeyRepository;
	private final AppUserRepository appUserRepository;
	private final SecureRandom secureRandom = new SecureRandom();

	public ApiKeyService(ApiKeyRepository apiKeyRepository, AppUserRepository appUserRepository) {
		this.apiKeyRepository = apiKeyRepository;
		this.appUserRepository = appUserRepository;
	}

	@Transactional(readOnly = true)
	public ApiKeyDto getForCurrentUser() {
		AppUser user = getCurrentUser();
		return apiKeyRepository.findByUser(user)
				.map(ApiKeyDto::fromEntity)
				.orElse(null);
	}

	@Transactional
	public ApiKeyDto createOrReplaceForCurrentUser() {
		AppUser user = getCurrentUser();
		String keyValue = generateKeyValue();
		LocalDateTime now = LocalDateTime.now();

		ApiKey apiKey = apiKeyRepository.findByUser(user)
				.map(existing -> {
					existing.setKeyValue(keyValue);
					existing.setCreatedAt(now);
					existing.setLastUsedAt(null);
					return existing;
				})
				.orElseGet(() -> new ApiKey(keyValue, now, user));

		return ApiKeyDto.fromEntity(apiKeyRepository.save(apiKey));
	}

	@Transactional
	public void deleteForCurrentUser() {
		AppUser user = getCurrentUser();
		apiKeyRepository.findByUser(user).ifPresent(apiKeyRepository::delete);
	}

	@Transactional
	public ApiKey resolveByKeyValue(String value) {
		ApiKey apiKey = apiKeyRepository.findByKeyValue(value)
				.orElseThrow(() -> new EntityNotFoundException("API key inválida."));

		apiKey.setLastUsedAt(LocalDateTime.now());
		return apiKeyRepository.save(apiKey);
	}

	private AppUser getCurrentUser() {
		String email = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.map(auth -> auth.getName())
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não autenticado."));

		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
	}

	private String generateKeyValue() {
		byte[] buffer = new byte[KEY_SIZE_BYTES];
		secureRandom.nextBytes(buffer);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
	}
}
