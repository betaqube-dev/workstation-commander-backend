package dev.betaqube.wc.configparam;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SystemParameterService {

	private final SystemParameterRepository repository;

	public SystemParameterService(SystemParameterRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<SystemParameter> findAll() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	public SystemParameter findByKey(String key) {
		return repository.findByKey(key)
				.orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado: " + key));
	}

	@Transactional
	public SystemParameter updateValue(String key, String newValue) {
		SystemParameter parameter = repository.findByKey(key)
				.orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado: " + key));

		parameter.setValue(newValue);
		return repository.save(parameter);
	}

	@Transactional
	public void delete(String key) {
		SystemParameter parameter = repository.findByKey(key)
				.orElseThrow(() -> new EntityNotFoundException("Parâmetro não encontrado: " + key));

		if (parameter.isFix()) {
			throw new IllegalStateException("Parâmetro fixo não pode ser excluído.");
		}

		repository.delete(parameter);
	}

	@Transactional
	public SystemParameter create(String key, String value, ParameterType type) {
		Optional<SystemParameter> existing = repository.findByKey(key);
		if (existing.isPresent()) {
			throw new IllegalStateException("Chave já existe: " + key);
		}
		SystemParameter parameter = new SystemParameter(key, value, type, false);
		return repository.save(parameter);
	}
}
