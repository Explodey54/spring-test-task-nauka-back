package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.workers.data.*;
import com.testtask.nauka.api.workers.dto.WorkdayResultStatusDto;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.common.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workday-result-statuses")
public class WorkdayResultStatusController extends AbstractCrudController<WorkdayResultStatus, WorkdayResultStatusDto> {
    public WorkdayResultStatusController(WorkdayResultStatusRepository repository) {
        super(repository, WorkdayResultStatus.class, WorkdayResultStatusDto.class);
    }

    @GetMapping
    public SuccessResponse<List<WorkdayResultStatusDto>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public SuccessResponse<WorkdayResultStatusDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public SuccessResponse<WorkdayResultStatusDto> create(
            @Validated(WorkdayResultStatusDto.Create.class) @RequestBody WorkdayResultStatusDto input
    ) {
        return super.create(input);
    }

    @PutMapping("/{id}")
    public SuccessResponse<WorkdayResultStatusDto> update(
            @Validated @RequestBody WorkdayResultStatusDto input,
            @PathVariable Long id
    ) {
        return super.update(input, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }
}
