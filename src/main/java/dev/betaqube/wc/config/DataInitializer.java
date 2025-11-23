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
		return args -> repository.findByUsername("admin").orElseGet(() -> {
			AppUser user = new AppUser("admin", passwordEncoder.encode("password"), "ROLE_ADMIN");
			return repository.save(user);
		});
	}
}
