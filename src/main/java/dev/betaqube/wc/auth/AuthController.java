package dev.betaqube.wc.auth;

import dev.betaqube.wc.security.JwtService;
import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final AppUserRepository userRepository;
	private final JwtService jwtService;

	public AuthController(AuthenticationManager authenticationManager, AppUserRepository userRepository, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email(), request.password())
		);

		if (!authentication.isAuthenticated()) {
			throw new UsernameNotFoundException("Authentication failed");
		}

		AppUser user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String token = jwtService.generateToken(user);
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
