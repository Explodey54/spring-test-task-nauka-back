package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.workers.dto.DepartmentDto;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.api.workers.data.Department;
import com.testtask.nauka.api.workers.data.DepartmentRepository;
import com.testtask.nauka.common.response.SuccessResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController extends AbstractCrudController<Department, DepartmentDto> {
    public DepartmentController(DepartmentRepository repository) {
        super(repository, Department.class, DepartmentDto.class);
    }

    @GetMapping
    public SuccessResponse<List<DepartmentDto>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public SuccessResponse<DepartmentDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public SuccessResponse<DepartmentDto> create(@Valid @RequestBody DepartmentDto input) {
        return super.create(input);
    }

    @PutMapping("/{id}")
    public SuccessResponse<DepartmentDto> update(@Valid @RequestBody DepartmentDto input, @PathVariable Long id) {
        return super.update(input, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }
}
