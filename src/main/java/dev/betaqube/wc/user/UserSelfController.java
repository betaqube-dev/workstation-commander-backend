package dev.betaqube.wc.user;

import dev.betaqube.wc.user.dto.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserSelfController {

	private final AppUserService appUserService;

	public UserSelfController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}

	@PutMapping("/change-password")
	public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		appUserService.changePassword(request);
		return ResponseEntity.noContent().build();
	}
}
