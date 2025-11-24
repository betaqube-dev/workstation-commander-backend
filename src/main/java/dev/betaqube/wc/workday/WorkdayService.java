package dev.betaqube.wc.workday;

import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import dev.betaqube.wc.configparam.SystemParameterService;
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
	private final SystemParameterService systemParameterService;

	private static final String PARAM_WORK_START = "wc_work_start";
	private static final String PARAM_LUNCH_PAUSE = "wc_lunch_pause";
	private static final String PARAM_LUNCH_RETURN = "wc_lunch_return";
	private static final String PARAM_WORK_END = "wc_work_end";

	public WorkdayService(WorkdayRepository workdayRepository, AppUserRepository appUserRepository,
			WorkdayMessagesProperties messages, SystemParameterService systemParameterService) {
		this.workdayRepository = workdayRepository;
		this.appUserRepository = appUserRepository;
		this.messages = messages;
		this.systemParameterService = systemParameterService;
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
		return getStatusForUser(user);
	}

	public WorkdayStatusDto getStatusForUser(AppUser user) {
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		DayOfWeek dayOfWeek = today.getDayOfWeek();

		List<String> alerts = new ArrayList<>();

		LocalTime startRef = readTimeParam(PARAM_WORK_START, LocalTime.of(9, 0));
		LocalTime lunchRef = readTimeParam(PARAM_LUNCH_PAUSE, LocalTime.of(12, 30));
		LocalTime lunchReturnRef = readTimeParam(PARAM_LUNCH_RETURN, LocalTime.of(13, 30));
		LocalTime endRef = readTimeParam(PARAM_WORK_END, LocalTime.of(18, 0));

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

	private LocalTime readTimeParam(String key, LocalTime defaultValue) {
		try {
			String value = systemParameterService.findByKey(key).getValue();
			String padded = value.length() == 3 ? "0" + value : value;
			if (padded.length() != 4) {
				return defaultValue;
			}
			int hours = Integer.parseInt(padded.substring(0, 2));
			int minutes = Integer.parseInt(padded.substring(2, 4));
			return LocalTime.of(hours, minutes);
		} catch (Exception ex) {
			return defaultValue;
		}
	}
}
