package dev.betaqube.wc.workday;

import dev.betaqube.wc.apikey.ApiKey;
import dev.betaqube.wc.apikey.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/workday")
public class PublicWorkdayController {

	private final ApiKeyService apiKeyService;
	private final WorkdayService workdayService;

	public PublicWorkdayController(ApiKeyService apiKeyService, WorkdayService workdayService) {
		this.apiKeyService = apiKeyService;
		this.workdayService = workdayService;
	}

	@GetMapping("/today")
	public ResponseEntity<WorkdayStatusDto> getTodayStatus(
			@RequestHeader(value = "X-API-Key", required = false) String apiKeyHeader,
			@RequestParam(value = "apiKey", required = false) String apiKeyParam
	) {
		String apiKeyValue = apiKeyHeader != null ? apiKeyHeader : apiKeyParam;
		if (apiKeyValue == null || apiKeyValue.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		ApiKey apiKey = apiKeyService.resolveByKeyValue(apiKeyValue);
		WorkdayStatusDto status = workdayService.getStatusForUser(apiKey.getUser());
		return ResponseEntity.ok(status);
	}
}
