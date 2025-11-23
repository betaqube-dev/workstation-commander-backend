package dev.betaqube.wc.workday;

import dev.betaqube.wc.user.AppUser;
import dev.betaqube.wc.user.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public Workday getToday() {
        LocalDate today = LocalDate.now();
        return workdayRepository.findByUserAndDate(getCurrentUser(), today).orElse(null);
	}

	public Workday startDay() {
		AppUser user = getCurrentUser();
		LocalDate today = LocalDate.now();
		workdayRepository.findByUserAndDate(user, today).ifPresent(existing -> {
			throw new WorkdayOperationException("Workday já iniciado");
		});

		Workday workday = new Workday(user, today, WorkdayState.WORKING);
		workday.setStartTime(LocalDateTime.now());
		return workdayRepository.save(workday);
	}

	public Workday pauseLunch() {
		Workday workday = getRequiredToday();
		if (workday.getState() != WorkdayState.WORKING) {
			throw new WorkdayOperationException("Workday não está em andamento");
		}
		workday.setLunchStartTime(LocalDateTime.now());
		workday.setState(WorkdayState.LUNCH_BREAK);
		return workdayRepository.save(workday);
	}

	public Workday returnFromLunch() {
		Workday workday = getRequiredToday();
		if (workday.getState() != WorkdayState.LUNCH_BREAK) {
			throw new WorkdayOperationException("Workday não está em pausa de almoço");
		}
		workday.setLunchEndTime(LocalDateTime.now());
		workday.setState(WorkdayState.WORKING);
		return workdayRepository.save(workday);
	}

	public Workday endDay() {
		Workday workday = getRequiredToday();
		if (workday.getState() == WorkdayState.ENDED) {
			throw new WorkdayOperationException("Workday já encerrado");
		}
		workday.setEndTime(LocalDateTime.now());
		workday.setState(WorkdayState.ENDED);
		return workdayRepository.save(workday);
	}

	private Workday getRequiredToday() {
		Workday workday = getToday();
		if (workday == null) {
			throw new WorkdayOperationException("Workday ainda não iniciado");
		}
		return workday;
	}
}
