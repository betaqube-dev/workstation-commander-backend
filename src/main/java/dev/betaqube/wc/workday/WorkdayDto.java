package dev.betaqube.wc.workday;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkdayDto {

    private Long id;
    private String state;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime lunchStartTime;
    private LocalDateTime lunchEndTime;
    private LocalDateTime endTime;

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
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

    public static WorkdayDto fromEntity(Workday workday) {
        WorkdayDto dto = new WorkdayDto();
        dto.id = workday.getId();
        dto.state = workday.getState() != null ? workday.getState().name() : null;
        dto.date = workday.getDate();
        dto.startTime = workday.getStartTime();
        dto.lunchStartTime = workday.getLunchStartTime();
        dto.lunchEndTime = workday.getLunchEndTime();
        dto.endTime = workday.getEndTime();
        return dto;
    }
}
