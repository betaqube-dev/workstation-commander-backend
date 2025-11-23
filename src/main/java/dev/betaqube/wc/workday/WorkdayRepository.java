package dev.betaqube.wc.workday;

import dev.betaqube.wc.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkdayRepository extends JpaRepository<Workday, Long> {

    Optional<Workday> findByUserAndDate(AppUser user, LocalDate date);
}
