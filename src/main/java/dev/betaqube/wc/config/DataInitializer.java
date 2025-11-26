package dev.betaqube.wc.config;

import dev.betaqube.wc.configparam.ParameterType;
import dev.betaqube.wc.configparam.SystemParameter;
import dev.betaqube.wc.configparam.SystemParameterRepository;
import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

	@Bean
	public CommandLineRunner seedDefaultUser(AppUserRepository repository, PasswordEncoder passwordEncoder) {
		return args -> repository.findByEmail("admin@wc.local").orElseGet(() -> {
			AppUser user = new AppUser("admin@wc.local", "Administrador", passwordEncoder.encode("password"), "ROLE_ADMIN");
			return repository.save(user);
		});
	}

	@Bean
	public CommandLineRunner seedSystemParameters(SystemParameterRepository repository) {
		return args -> {
			seedIfMissing(repository, "wc_work_start", "900");
			seedIfMissing(repository, "wc_lunch_pause", "1230");
			seedIfMissing(repository, "wc_lunch_return", "1330");
			seedIfMissing(repository, "wc_work_end", "1800");
		};
	}

	private void seedIfMissing(SystemParameterRepository repository, String key, String value) {
		repository.findByKey(key).orElseGet(() -> repository.save(new SystemParameter(key, value, ParameterType.INT, true)));
	}
}
