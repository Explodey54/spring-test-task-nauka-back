package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.workers.dto.WorkdayResultDto;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.common.exceptions.RelationNotFoundException;
import com.testtask.nauka.api.workers.data.WorkdayResult;
import com.testtask.nauka.api.workers.data.WorkdayResultRepository;
import com.testtask.nauka.api.workers.data.WorkerRepository;
import com.testtask.nauka.common.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workday-results")
public class WorkdayResultController extends AbstractCrudController<WorkdayResult, WorkdayResultDto> {
    private final WorkerRepository workerRepository;

    @Autowired
    public WorkdayResultController(WorkdayResultRepository workdayResultRepository, WorkerRepository workerRepository) {
        super(workdayResultRepository, WorkdayResult.class, WorkdayResultDto.class);
        this.workerRepository = workerRepository;
    }

    @GetMapping
    public SuccessResponse<List<WorkdayResultDto>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public SuccessResponse<WorkdayResultDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public SuccessResponse<WorkdayResultDto> create(@Valid @RequestBody WorkdayResultDto input) {
        checkRelations(input);
        return super.create(input);
    }

    @PutMapping("/{id}")
    public SuccessResponse<WorkdayResultDto> update(@Valid @RequestBody WorkdayResultDto input, @PathVariable Long id) {
        checkRelations(input);
        return super.update(input, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }

    private void checkRelations(WorkdayResultDto input) throws RelationNotFoundException {
        Long workerId = input.getWorkerId();
        if (workerId != null && workerRepository.findById(workerId).isEmpty()) {
            throw new RelationNotFoundException("Worker is not found", "worker");
        }
    }
}
