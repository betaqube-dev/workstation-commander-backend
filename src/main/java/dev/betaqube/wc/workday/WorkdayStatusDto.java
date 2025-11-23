package dev.betaqube.wc.workday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkdayStatusDto {

	private Long id;
	private String state;
	private LocalDate date;
	private LocalDateTime startTime;
	private LocalDateTime lunchStartTime;
	private LocalDateTime lunchEndTime;
	private LocalDateTime endTime;
	private List<String> alerts = new ArrayList<>();

	public String getState() {
		return state;
	}

	public Long getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getLunchStartTime() {
		return lunchStartTime;
	}

	public LocalDateTime getLunchEndTime() {
		return lunchEndTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public List<String> getAlerts() {
		return alerts;
	}

	public static WorkdayStatusDto noSession(List<String> alerts) {
		WorkdayStatusDto dto = new WorkdayStatusDto();
		dto.state = "NO_SESSION";
		if (alerts != null) {
			dto.alerts.addAll(alerts);
		}
		return dto;
	}

	public static WorkdayStatusDto fromEntity(Workday workday, List<String> alerts) {
		WorkdayStatusDto dto = new WorkdayStatusDto();
		dto.id = workday.getId();
		dto.state = workday.getState() != null ? workday.getState().name() : null;
		dto.date = workday.getDate();
		dto.startTime = workday.getStartTime();
		dto.lunchStartTime = workday.getLunchStartTime();
		dto.lunchEndTime = workday.getLunchEndTime();
		dto.endTime = workday.getEndTime();
		if (alerts != null) {
			dto.alerts.addAll(alerts);
		}
		return dto;
	}
}
