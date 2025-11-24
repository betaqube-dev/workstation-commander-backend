package dev.betaqube.wc.user;

import dev.betaqube.wc.user.dto.AppUserDto;
import dev.betaqube.wc.user.dto.ChangePasswordRequest;
import dev.betaqube.wc.user.dto.CreateUserRequest;
import dev.betaqube.wc.user.dto.UpdateUserRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<AppUserDto> listAll() {
		return appUserRepository.findAll().stream()
				.map(AppUserDto::fromEntity)
				.toList();
	}

	public AppUserDto getById(Long id) {
		return AppUserDto.fromEntity(
				appUserRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id))
		);
	}

	public AppUserDto createUser(CreateUserRequest request) {
		appUserRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
			throw new IllegalStateException("E-mail já está em uso.");
		});

		AppUser user = new AppUser(
				request.getEmail(),
				request.getName(),
				passwordEncoder.encode(request.getPassword()),
				ROLE_USER
		);

		return AppUserDto.fromEntity(appUserRepository.save(user));
	}

	public AppUserDto updateUser(Long id, UpdateUserRequest request) {
		AppUser user = appUserRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

		ensureNotAdmin(user);

		appUserRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
			if (!existing.getId().equals(id)) {
				throw new IllegalStateException("E-mail já está em uso.");
			}
		});

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		return AppUserDto.fromEntity(appUserRepository.save(user));
	}

	public void deleteUser(Long id) {
		AppUser user = appUserRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

		ensureNotAdmin(user);
		appUserRepository.delete(user);
	}

	public void changePassword(ChangePasswordRequest request) {
		AppUser user = getCurrentUser();
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new IllegalStateException("Senha atual incorreta.");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		appUserRepository.save(user);
	}

	private void ensureNotAdmin(AppUser user) {
		if (ROLE_ADMIN.equalsIgnoreCase(user.getRole())) {
			throw new IllegalStateException("Operação não permitida para usuários ADMIN.");
		}
	}

	private AppUser getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
	}
}
