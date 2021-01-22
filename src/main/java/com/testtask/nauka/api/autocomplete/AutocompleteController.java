package com.testtask.nauka.api.autocomplete;

import com.testtask.nauka.api.auth.data.UserRepository;
import com.testtask.nauka.api.autocomplete.dto.AutocompleteDto;
import com.testtask.nauka.api.calendar.data.CalendarDayStatusRepository;
import com.testtask.nauka.api.workers.data.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/autocomplete")
public class AutocompleteController {
    private final WorkerRepository workerRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CalendarDayStatusRepository calendarDayStatusRepository;
    private final WorkdayResultStatusRepository workdayResultStatusRepository;

    public AutocompleteController(
            WorkerRepository workerRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            CalendarDayStatusRepository calendarDayStatusRepository,
            WorkdayResultStatusRepository workdayResultStatusRepository) {
        this.workerRepository = workerRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.calendarDayStatusRepository = calendarDayStatusRepository;
        this.workdayResultStatusRepository = workdayResultStatusRepository;
    }

    @GetMapping("/workers")
    public List<AutocompleteDto<Long>> getWorkers() {
        return workerRepository.findAll().stream()
                .map(i -> new AutocompleteDto<>(i.getId(), String.format("%s %s", i.getFirstName(), i.getLastName())))
                .collect(Collectors.toList());
    }

    @GetMapping("/users")
    public List<AutocompleteDto<Long>> getUsers() {
        return userRepository.findAll().stream()
                .map(i -> new AutocompleteDto<>(i.getId(), i.getUsername()))
                .collect(Collectors.toList());
    }

    @GetMapping("/departments")
    public List<AutocompleteDto<Long>> getDepartments() {
        return departmentRepository.findAll().stream()
                .map(i -> new AutocompleteDto<>(i.getId(), i.getTitle()))
                .collect(Collectors.toList());
    }

    @GetMapping("/calendar-day-status")
    public List<AutocompleteDto<Long>> getCalendarDayStatuses() {
        return this.calendarDayStatusRepository.findAll().stream()
                .map(i -> new AutocompleteDto<>(i.getId(), i.getTitle()))
                .collect(Collectors.toList());
    }

    @GetMapping("/workday-result-status")
    public List<AutocompleteDto<Long>> getWorkdayResultStatuses() {
        return this.workdayResultStatusRepository.findAll().stream()
                .map(i -> new AutocompleteDto<>(i.getId(), i.getTitle()))
                .collect(Collectors.toList());
    }

}
