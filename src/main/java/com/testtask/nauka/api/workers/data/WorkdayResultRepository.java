package com.testtask.nauka.api.workers.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface WorkdayResultRepository extends JpaRepository<WorkdayResult, Long> {
    List<WorkdayResult> findAllByWorkerAndDateBetween(Worker worker, Calendar from, Calendar to);
}