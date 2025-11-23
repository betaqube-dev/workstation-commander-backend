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

	public WorkdayService(WorkdayRepository workdayRepository, AppUserRepository appUserRepository) {
		this.workdayRepository = workdayRepository;
		this.appUserRepository = appUserRepository;
	}

	private AppUser getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));
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
			throw new WorkdayOperationException("Nao e dia util para iniciar o trabalho");
		}
		workdayRepository.findByUserAndDate(user, today).ifPresent(existing -> {
			throw new WorkdayOperationException("Workday ja iniciado");
		});

		Workday workday = new Workday(user, today, WorkdayState.WORKING);
		workday.setStartTime(LocalDateTime.now());
		return workdayRepository.save(workday);
	}

	public Workday pauseLunch() {
		Workday workday = getRequiredToday();
		if (workday.getState() != WorkdayState.WORKING) {
			throw new WorkdayOperationException("Workday nao esta em andamento");
		}
		if (workday.getLunchStartTime() != null) {
			throw new WorkdayOperationException("Pausa de almoco ja registrada para hoje");
		}
		workday.setLunchStartTime(LocalDateTime.now());
		workday.setState(WorkdayState.LUNCH_BREAK);
		return workdayRepository.save(workday);
	}

	public Workday returnFromLunch() {
		Workday workday = getRequiredToday();
		if (workday.getState() != WorkdayState.LUNCH_BREAK) {
			throw new WorkdayOperationException("Workday nao esta em pausa de almoco");
		}
		workday.setLunchEndTime(LocalDateTime.now());
		workday.setState(WorkdayState.WORKING);
		return workdayRepository.save(workday);
	}

	public Workday endDay() {
		Workday workday = getRequiredToday();
		if (workday.getState() == WorkdayState.ENDED) {
			throw new WorkdayOperationException("Workday ja encerrado");
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
		LocalTime endRef = LocalTime.of(14, 0);

		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return WorkdayStatusDto.noSession(alerts);
		}

		Workday workday = workdayRepository.findByUserAndDate(user, today).orElse(null);
		if (workday == null) {
			if (now.isAfter(startRef)) {
				alerts.add("E ai, nao vai trabalhar?");
			}
			return WorkdayStatusDto.noSession(alerts);
		}

		if (workday.getState() == WorkdayState.WORKING) {
			if (workday.getLunchStartTime() == null && now.isAfter(lunchRef)) {
				alerts.add("Voce ainda nao marcou pausa para almoco.");
			}
			if (now.isAfter(endRef) && workday.getEndTime() == null) {
				alerts.add("Seu expediente ja passou das 18h, deseja encerrar o dia?");
			}
		} else if (workday.getState() == WorkdayState.LUNCH_BREAK) {
			if (now.isAfter(lunchReturnRef)) {
				alerts.add("Vai voltar do almoco?");
			}
		}

		return WorkdayStatusDto.fromEntity(workday, alerts);
	}

	private Workday getRequiredToday() {
		Workday workday = getToday();
		if (workday == null) {
			throw new WorkdayOperationException("Workday ainda nao iniciado");
		}
		return workday;
	}
}
