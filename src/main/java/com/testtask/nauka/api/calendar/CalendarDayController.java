package com.testtask.nauka.api.calendar;

import com.testtask.nauka.api.calendar.data.CalendarDay;
import com.testtask.nauka.api.calendar.dto.CalendarDayDto;
import com.testtask.nauka.api.calendar.data.CalendarDayRepository;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.common.exceptions.InvalidFieldsException;
import com.testtask.nauka.common.exceptions.UniqueFieldDuplicationException;
import com.testtask.nauka.common.response.SuccessResponse;
import com.testtask.nauka.common.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/calendar-days")
public class CalendarDayController extends AbstractCrudController<CalendarDay, CalendarDayDto> {
    private final DateTimeService dateTimeService;
    private final CalendarDayRepository calendarDayRepository;

    @Autowired
    public CalendarDayController(CalendarDayRepository calendarDayRepository, DateTimeService dateTimeService) {
        super(calendarDayRepository, CalendarDay.class, CalendarDayDto.class);
        this.calendarDayRepository = calendarDayRepository;
        this.dateTimeService = dateTimeService;
    }

    @GetMapping
    public SuccessResponse<List<CalendarDayDto>> getAll(
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month", required = false) Integer month
    ) {
        if (month != null && (month > 12 || month < 1)) {
            throw new InvalidFieldsException("Month param is out of bounds!", "month", "Out of bounds (1-12)");
        }

        Calendar from = dateTimeService.getTodayCalendar();
        Calendar to = dateTimeService.getTodayCalendar();

        if (year != null) {
            from.set(Calendar.YEAR, year);
            to.set(Calendar.YEAR, year);
        }

        if (month != null) {
            from.set(Calendar.MONTH, month - 1);
            to.set(Calendar.MONTH, month - 1);
        }

        from.set(Calendar.DAY_OF_MONTH, 1);
        to.set(Calendar.DAY_OF_MONTH, to.getActualMaximum(Calendar.DAY_OF_MONTH));

        List<CalendarDayDto> output = calendarDayRepository.findAllByDateBetween(from, to).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return new SuccessResponse<>(output);
    }

    @GetMapping("/{id}")
    public SuccessResponse<CalendarDayDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public SuccessResponse<CalendarDayDto> create(@Validated(CalendarDayDto.Create.class) @RequestBody CalendarDayDto input) {
        checkUniqueFields(input);
        return super.create(input);
    }

    @PutMapping("/{id}")
    public SuccessResponse<CalendarDayDto> update(@Validated(CalendarDayDto.Update.class) @RequestBody CalendarDayDto input, @PathVariable Long id) {
        checkUniqueFields(input, id);
        return super.update(input, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }

    private void checkUniqueFields(CalendarDayDto input) {
        checkUniqueFields(input, null);
    }

    // Check if CalendarDay with date in dto exists, then throw
    // If id is provided also check that found CalendarDay has that id
    private void checkUniqueFields(CalendarDayDto input, Long id) {
        Optional<CalendarDay> found = calendarDayRepository.findByDate(input.getDate());
        if (found.isEmpty()) return;
        if (id != null && id.equals(found.get().getId())) return;

        throw new UniqueFieldDuplicationException("Calendar day with this date already exists", "date");
    }

}
