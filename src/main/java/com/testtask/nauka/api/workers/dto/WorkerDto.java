package com.testtask.nauka.api.workers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.dto.UserDto;
import com.testtask.nauka.api.workers.data.Department;
import com.testtask.nauka.api.workers.data.WorkdayResult;
import com.testtask.nauka.api.workers.data.Worker;
import com.testtask.nauka.common.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import static com.testtask.nauka.common.utils.UtilHelpers.ifNotNull;

@Getter @Setter
@NoArgsConstructor
@ApiModel("Worker")
public class WorkerDto extends BaseDto<WorkerDto, Worker> {

    private UserDto user;
    private DepartmentDto department;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long departmentId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    @JsonIgnoreProperties({"worker"})
    private List<WorkdayResultDto> workdayResults;

    private String firstName;
    private String lastName;

    public WorkerDto(Worker entity) {
        this(entity, entity.getDepartment(), entity.getUser(), entity.getWorkdayResults());
    }

    public WorkerDto(Worker worker, Department department, User user, List<WorkdayResult> workdayResults) {
        super(worker);
        firstName = worker.getFirstName();
        lastName = worker.getLastName();
        this.user = ifNotNull(user, UserDto::new);
        this.department = ifNotNull(department, DepartmentDto::new);
        userId = ifNotNull(this.user, BaseDto::getId);
        departmentId = ifNotNull(this.department, BaseDto::getId);
        this.workdayResults = ifNotNull(workdayResults, i -> i.stream()
                .map(WorkdayResultDto::new)
                .collect(Collectors.toList()));
    }

    @Override
    public Worker toEntity() {
        Worker worker = new Worker();
        worker.setFirstName(firstName);
        worker.setLastName(lastName);
        worker.setUser(ifNotNull(userId, User::new));
        worker.setDepartment(ifNotNull(departmentId, Department::new));
        worker.setWorkdayResults(ifNotNull(workdayResults, i -> i.stream()
                .map(WorkdayResultDto::toEntity)
                .collect(Collectors.toList())));
        return worker;
    }


}
