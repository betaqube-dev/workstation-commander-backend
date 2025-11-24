package dev.betaqube.wc.configparam;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system-params")
@PreAuthorize("hasRole('ADMIN')")
public class SystemParameterController {

	private final SystemParameterService systemParameterService;

	public SystemParameterController(SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	@GetMapping
	public List<SystemParameterDto> findAll() {
		return systemParameterService.findAll().stream()
				.map(SystemParameterDto::fromEntity)
				.toList();
	}

	@GetMapping("/{key}")
	public SystemParameterDto findByKey(@PathVariable String key) {
		return SystemParameterDto.fromEntity(systemParameterService.findByKey(key));
	}

	@PutMapping("/{key}")
	public SystemParameterDto updateValue(@PathVariable String key, @RequestBody UpdateValueRequest request) {
		SystemParameter updated = systemParameterService.updateValue(key, request.value());
		return SystemParameterDto.fromEntity(updated);
	}

	@PostMapping
	public ResponseEntity<SystemParameterDto> create(@RequestBody CreateParameterRequest request) {
		SystemParameter created = systemParameterService.create(request.key(), request.value(), request.type());
		return ResponseEntity.status(HttpStatus.CREATED).body(SystemParameterDto.fromEntity(created));
	}

	@DeleteMapping("/{key}")
	public ResponseEntity<Void> delete(@PathVariable String key) {
		systemParameterService.delete(key);
		return ResponseEntity.noContent().build();
	}

	public record UpdateValueRequest(@NotBlank String value) {
	}

	public record CreateParameterRequest(@NotBlank String key, @NotBlank String value, ParameterType type) {
	}
}
