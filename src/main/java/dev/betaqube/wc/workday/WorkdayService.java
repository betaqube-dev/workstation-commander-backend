package dev.betaqube.wc.workday;

import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkdayService {

	private final WorkdayRepository workdayRepository;
	private final AppUserRepository appUserRepository;
	private final WorkdayMessagesProperties messages;

	public WorkdayService(WorkdayRepository workdayRepository, AppUserRepository appUserRepository,
			WorkdayMessagesProperties messages) {
		this.workdayRepository = workdayRepository;
		this.appUserRepository = appUserRepository;
		this.messages = messages;
	}

	private AppUser getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(messages.getUserNotFound()));
	}

	public Workday getToday() {
		LocalDate today = LocalDate.now();
		return workdayRepository.findByUserAndDate(getCurrentUser(), today).orElse(null);
	}

	public Workday startDay() {
		AppUser user = getCurrentUser();
		LocalDate today = LocalDate.now();
		DayOfWeek dayOfWeek = today.getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			throw new WorkdayOperationException(messages.getWeekendStart());
		}
		workdayRepository.findByUserAndDate(user, today).ifPresent(existing -> {
			throw new WorkdayOperationException(messages.getAlreadyStarted());
		});

		Workday workday = new Workday(user, today, WorkdayState.WORKING);
		workday.setStartTime(LocalDateTime.now());
		return workdayRepository.save(workday);
	}

	public Workday pauseLunch() {
		Workday workday = getRequiredToday();
		if (workday.getState() != WorkdayState.WORKING) {
			throw new WorkdayOperationException(messages.getNotInProgress());
		}
		if (workday.getLunchStartTime() != null) {
			throw new WorkdayOperationException(messages.getLunchAlreadyMarked());
		}
		workday.setLunchStartTime(LocalDateTime.now());
		workday.setState(WorkdayState.LUNCH_BREAK);
		return workdayRepository.save(workday);
	}

	public Workday returnFromLunch() {
		Workday workday = getRequiredToday();
		if (workday.getState() != WorkdayState.LUNCH_BREAK) {
			throw new WorkdayOperationException(messages.getNotOnLunch());
		}
		workday.setLunchEndTime(LocalDateTime.now());
		workday.setState(WorkdayState.WORKING);
		return workdayRepository.save(workday);
	}

	public Workday endDay() {
		Workday workday = getRequiredToday();
		if (workday.getState() == WorkdayState.ENDED) {
			throw new WorkdayOperationException(messages.getAlreadyEnded());
		}
		workday.setEndTime(LocalDateTime.now());
		workday.setState(WorkdayState.ENDED);
		return workdayRepository.save(workday);
	}

	public WorkdayStatusDto getStatus() {
		AppUser user = getCurrentUser();
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		DayOfWeek dayOfWeek = today.getDayOfWeek();

		List<String> alerts = new ArrayList<>();

		LocalTime startRef = LocalTime.of(9, 0);
		LocalTime lunchRef = LocalTime.of(12, 30);
		LocalTime lunchReturnRef = LocalTime.of(13, 30);
		LocalTime endRef = LocalTime.of(18, 0);

		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return WorkdayStatusDto.noSession(alerts);
		}

		Workday workday = workdayRepository.findByUserAndDate(user, today).orElse(null);
		if (workday == null) {
			if (now.isAfter(startRef)) {
				alerts.add(messages.getAlertNoSessionAfterStart());
			}
			return WorkdayStatusDto.noSession(alerts);
		}

		if (workday.getState() == WorkdayState.WORKING) {
			if (workday.getLunchStartTime() == null && now.isAfter(lunchRef)) {
				alerts.add(messages.getAlertMissingLunch());
			}
			if (now.isAfter(endRef) && workday.getEndTime() == null) {
				alerts.add(messages.getAlertPastEnd());
			}
		} else if (workday.getState() == WorkdayState.LUNCH_BREAK) {
			if (now.isAfter(lunchReturnRef)) {
				alerts.add(messages.getAlertReturnFromLunch());
			}
		}

		return WorkdayStatusDto.fromEntity(workday, alerts);
	}

	private Workday getRequiredToday() {
		Workday workday = getToday();
		if (workday == null) {
			throw new WorkdayOperationException(messages.getNotStarted());
		}
		return workday;
	}
}
