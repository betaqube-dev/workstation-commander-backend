package dev.betaqube.wc.apikey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/api-key")
public class ApiKeyController {

	private final ApiKeyService apiKeyService;

	public ApiKeyController(ApiKeyService apiKeyService) {
		this.apiKeyService = apiKeyService;
	}

	@GetMapping
	public ResponseEntity<ApiKeyDto> getCurrentApiKey() {
		ApiKeyDto apiKey = apiKeyService.getForCurrentUser();
		if (apiKey == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(apiKey);
	}

	@PostMapping
	public ResponseEntity<ApiKeyDto> createOrReplace() {
		ApiKeyDto apiKey = apiKeyService.createOrReplaceForCurrentUser();
		return ResponseEntity.status(HttpStatus.CREATED).body(apiKey);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteCurrentApiKey() {
		apiKeyService.deleteForCurrentUser();
		return ResponseEntity.noContent().build();
	}
}
