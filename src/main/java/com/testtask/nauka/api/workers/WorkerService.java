package com.testtask.nauka.api.workers;

import com.testtask.nauka.common.exceptions.RecordNotFoundException;
import com.testtask.nauka.common.utils.DateTimeService;
import com.testtask.nauka.api.workers.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class WorkerService {
    private final WorkerRepository workerRep;
    private final DepartmentRepository departmentRep;
    private final WorkdayResultRepository workdayResultRep;
    private final DateTimeService dateTimeService;

    @Autowired
    public WorkerService(
            WorkerRepository workerRep,
            DepartmentRepository departmentRep,
            WorkdayResultRepository workdayResultRep,
            DateTimeService dateTimeService) {
        this.workerRep = workerRep;
        this.departmentRep = departmentRep;
        this.workdayResultRep = workdayResultRep;
        this.dateTimeService = dateTimeService;
    }

    public List<Worker> getWorkers(Long departmentId, Integer workdayMonth)
            throws RecordNotFoundException, IllegalArgumentException
    {
        // Check if month is in bounds
        if (workdayMonth != null && (workdayMonth < 1 || workdayMonth > 12)) {
            throw new IllegalArgumentException("Workday month is out of bounds.");
        }

        // Create calendars with first and last day of current month
        Calendar from = this.dateTimeService.getTodayCalendar();
        Calendar to = this.dateTimeService.getTodayCalendar();

        // If input month != null, set calendars to this month
        if (workdayMonth != null) {
            from.set(Calendar.MONTH, workdayMonth - 1);
            to.set(Calendar.MONTH, workdayMonth - 1);
        }

        // Set calendars to first and last day of the month
        from.set(Calendar.DAY_OF_MONTH, 1);
        to.set(Calendar.DAY_OF_MONTH, to.getActualMaximum(Calendar.DAY_OF_MONTH));

        if (departmentId == null) {
            return findWorkers(from, to);
        } else {
            return findWorkersByDepartment(departmentId, from, to);
        }
    }

    private List<Worker> findWorkers(Calendar workdayFrom, Calendar workdayTo) {
        List<Worker> output = workerRep.findAll();
        output.forEach(i -> {
            List<WorkdayResult> results = workdayResultRep.findAllByWorkerAndDateBetween(i, workdayFrom, workdayTo);
            i.setWorkdayResults(results);
        });
        return output;
    }

    private List<Worker> findWorkersByDepartment(Long departmentId, Calendar from, Calendar to)
            throws RecordNotFoundException
    {
        if (departmentRep.findById(departmentId).isEmpty()) {
            throw new RecordNotFoundException("Department not found");
        }

        List<Worker> output = workerRep.findAllByDepartmentId(departmentId);
        output.forEach(i -> i.setWorkdayResults(
                workdayResultRep.findAllByWorkerAndDateBetween(i, from, to)
        ));
        return output;
    }
}
