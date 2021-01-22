package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.data.UserRepository;
import com.testtask.nauka.api.workers.data.*;
import com.testtask.nauka.api.workers.dto.WorkerDto;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.common.exceptions.RecordNotFoundException;
import com.testtask.nauka.common.exceptions.RelationNotEmptyException;
import com.testtask.nauka.common.exceptions.RelationNotFoundException;
import com.testtask.nauka.common.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/workers")
public class WorkerController extends AbstractCrudController<Worker, WorkerDto> {
    private final WorkerService workerService;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public WorkerController(
            WorkerService workerService,
            WorkerRepository workerRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository) {
        super(workerRepository, Worker.class, WorkerDto.class);
        this.workerService = workerService;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public SuccessResponse<List<WorkerDto>> getAll(
            @RequestParam(name = "department", required = false) Long departmentId,
            @RequestParam(name = "workday_month", required = false) Integer workdayMonth
    ) {
        try {
            List<WorkerDto> output = workerService.getWorkers(departmentId, workdayMonth)
                    .stream()
                    .map(this::entityToDto)
                    .collect(Collectors.toList());
            return new SuccessResponse<>(output);
        } catch (RecordNotFoundException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public SuccessResponse<WorkerDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public SuccessResponse<WorkerDto> create(@Valid @RequestBody WorkerDto input) {
        checkRelations(input);
        return super.create(input);
    }

    @PutMapping("/{id}")
    public SuccessResponse<WorkerDto> update(@Valid @RequestBody WorkerDto input, @PathVariable Long id) {
        checkRelations(input);
        return super.update(input, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }

    // If user.id exists, check that exists in db and doesnt have worker attached
    // If department.id exists, check that exists in db
    private void checkRelations(WorkerDto input) throws RelationNotFoundException, RelationNotEmptyException {
        Long departmentId = input.getDepartmentId();
        Long userId = input.getUserId();
        if (departmentId != null && departmentRepository.findById(departmentId).isEmpty()) {
            throw new RelationNotFoundException("Department does not exist", "department");
        }

        if (userId != null) {
            Optional<User> foundUser = userRepository.findById(userId);
            if (foundUser.isEmpty()) {
                throw new RelationNotFoundException("User does not exist", "user");
            }

            if (foundUser.get().getWorker() != null) {
                throw new RelationNotEmptyException("User already have worker", "user");
            }
        }
    }
}
