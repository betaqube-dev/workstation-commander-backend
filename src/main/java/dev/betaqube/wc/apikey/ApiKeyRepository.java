package dev.betaqube.wc.apikey;

import dev.betaqube.wc.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
	Optional<ApiKey> findByUser(AppUser user);
	Optional<ApiKey> findByKeyValue(String keyValue);
}
