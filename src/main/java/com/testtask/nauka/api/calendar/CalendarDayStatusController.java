package com.testtask.nauka.api.calendar;

import com.testtask.nauka.api.calendar.data.CalendarDayStatus;
import com.testtask.nauka.api.calendar.data.CalendarDayStatusRepository;
import com.testtask.nauka.api.calendar.dto.CalendarDayStatusDto;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.common.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar-day-statuses")
public class CalendarDayStatusController extends AbstractCrudController<CalendarDayStatus, CalendarDayStatusDto> {
    private final CalendarDayStatusRepository calendarDayStatusRepository;

    @Autowired
    public CalendarDayStatusController(CalendarDayStatusRepository calendarDayStatusRepository) {
        super(calendarDayStatusRepository, CalendarDayStatus.class, CalendarDayStatusDto.class);
        this.calendarDayStatusRepository = calendarDayStatusRepository;
    }

    @GetMapping
    public SuccessResponse<List<CalendarDayStatusDto>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public SuccessResponse<CalendarDayStatusDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public SuccessResponse<CalendarDayStatusDto> create(
            @Validated(CalendarDayStatusDto.Create.class) @RequestBody CalendarDayStatusDto input
    ) {
        if (input.isDefault()) {
            calendarDayStatusRepository.setIsDefaultForAll(false);
        }
        return super.create(input);
    }

    @PutMapping("/{id}")
    public SuccessResponse<CalendarDayStatusDto> update(
            @Validated @RequestBody CalendarDayStatusDto input,
            @PathVariable Long id)
    {
        if (input.isDefault()) {
            System.out.println("Sett all");
            calendarDayStatusRepository.setIsDefaultForAll(false);
        }
        return super.update(input, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }
}
