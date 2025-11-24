package dev.betaqube.wc.config;

import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner seedDefaultUser(AppUserRepository repository, PasswordEncoder passwordEncoder) {
		return args -> repository.findByEmail("admin@wc.local").orElseGet(() -> {
			AppUser user = new AppUser("admin@wc.local", "Administrador", passwordEncoder.encode("password"), "ROLE_ADMIN");
			return repository.save(user);
		});
	}
}
