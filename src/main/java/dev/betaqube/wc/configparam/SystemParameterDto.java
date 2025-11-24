package dev.betaqube.wc.configparam;

public class SystemParameterDto {

	private Long id;
	private String key;
	private String value;
	private ParameterType type;
	private boolean fix;

	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public ParameterType getType() {
		return type;
	}

	public boolean isFix() {
		return fix;
	}

	public static SystemParameterDto fromEntity(SystemParameter parameter) {
		SystemParameterDto dto = new SystemParameterDto();
		dto.id = parameter.getId();
		dto.key = parameter.getKey();
		dto.value = parameter.getValue();
		dto.type = parameter.getType();
		dto.fix = parameter.isFix();
		return dto;
	}
}
