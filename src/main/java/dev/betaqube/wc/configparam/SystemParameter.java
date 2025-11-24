package dev.betaqube.wc.configparam;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "system_parameters")
public class SystemParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "param_key", nullable = false, unique = true)
	private String key;

	@Column(name = "param_value", nullable = false)
	private String value;

	@Enumerated(EnumType.STRING)
	private ParameterType type;

	@Column(nullable = false)
	private boolean fix;

	protected SystemParameter() {
	}

	public SystemParameter(String key, String value, ParameterType type, boolean fix) {
		this.key = key;
		this.value = value;
		this.type = type;
		this.fix = fix;
	}

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ParameterType getType() {
		return type;
	}

	public void setType(ParameterType type) {
		this.type = type;
	}

	public boolean isFix() {
		return fix;
	}

	public void setFix(boolean fix) {
		this.fix = fix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SystemParameter that)) return false;
		return Objects.equals(id, that.id) && Objects.equals(key, that.key);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, key);
	}
}
