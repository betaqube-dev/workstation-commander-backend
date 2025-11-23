package dev.betaqube.wc.workday;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workday")
public class WorkdayController {

    private final WorkdayService workdayService;

    public WorkdayController(WorkdayService workdayService) {
        this.workdayService = workdayService;
    }

    @GetMapping("/today")
    public ResponseEntity<WorkdayDto> getToday() {
        Workday workday = workdayService.getToday();
        if (workday == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(WorkdayDto.fromEntity(workday));
    }

    @PostMapping("/start")
    public ResponseEntity<WorkdayDto> startDay() {
        Workday workday = workdayService.startDay();
        return ResponseEntity.ok(WorkdayDto.fromEntity(workday));
    }

    @PostMapping("/pause-lunch")
    public ResponseEntity<WorkdayDto> pauseLunch() {
        Workday workday = workdayService.pauseLunch();
        return ResponseEntity.ok(WorkdayDto.fromEntity(workday));
    }

    @PostMapping("/return-lunch")
    public ResponseEntity<WorkdayDto> returnLunch() {
        Workday workday = workdayService.returnFromLunch();
        return ResponseEntity.ok(WorkdayDto.fromEntity(workday));
    }

    @PostMapping("/end")
    public ResponseEntity<WorkdayDto> endDay() {
        Workday workday = workdayService.endDay();
        return ResponseEntity.ok(WorkdayDto.fromEntity(workday));
    }
}
