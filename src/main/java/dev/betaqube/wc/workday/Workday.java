package dev.betaqube.wc.workday;

import dev.betaqube.wc.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "workday")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkdayState state;

    private LocalDateTime startTime;
    private LocalDateTime lunchStartTime;
    private LocalDateTime lunchEndTime;
    private LocalDateTime endTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Workday() {
    }

    public Workday(AppUser user, LocalDate date, WorkdayState state) {
        this.user = Objects.requireNonNull(user);
        this.date = Objects.requireNonNull(date);
        this.state = Objects.requireNonNull(state);
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WorkdayState getState() {
        return state;
    }

    public void setState(WorkdayState state) {
        this.state = state;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getLunchStartTime() {
        return lunchStartTime;
    }

    public void setLunchStartTime(LocalDateTime lunchStartTime) {
        this.lunchStartTime = lunchStartTime;
    }

    public LocalDateTime getLunchEndTime() {
        return lunchEndTime;
    }

    public void setLunchEndTime(LocalDateTime lunchEndTime) {
        this.lunchEndTime = lunchEndTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Workday workday)) {
            return false;
        }
        return Objects.equals(id, workday.id) && Objects.equals(user, workday.user) && Objects.equals(date, workday.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, date);
    }
}
