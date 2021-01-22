package com.testtask.nauka.api.auth.dto;

import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.workers.data.Department;
import com.testtask.nauka.api.workers.data.Worker;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("RegisterResponse")
@Getter @Setter
@NoArgsConstructor
public class RegisterResponseDto {
    private Long userId;
    private Long workerId;
    private String username;
    private RoleEnum roleId;
    private Long departmentId;
    private String firstName;
    private String lastName;

    public RegisterResponseDto(User user, Worker worker, Department department) {
        userId = user.getId();
        username = user.getUsername();
        roleId = user.getRole();

        if (worker != null) {
            workerId = worker.getId();
            firstName = worker.getFirstName();
            lastName = worker.getLastName();
        }

        if (department != null) {
            departmentId = department.getId();
        }
    }
}
