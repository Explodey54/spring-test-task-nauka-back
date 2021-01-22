package com.testtask.nauka.api.calendar.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CalendarDayRepository extends JpaRepository<CalendarDay, Long> {
    List<CalendarDay> findAllByDateBetween(Calendar from, Calendar to);
    Optional<CalendarDay> findByDate(Calendar calendar);
}
