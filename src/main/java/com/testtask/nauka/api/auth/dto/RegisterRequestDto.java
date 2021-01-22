package com.testtask.nauka.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testtask.nauka.api.auth.data.RoleEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel("RegisterRequest")
@Getter @Setter
public class RegisterRequestDto {
    @Size(min = 4)
    private String username;
    @Size(min = 6)
    private String password;
    @NotEmpty
    private String firstName;
    private String lastName;
    private Long departmentId;
//    private RoleEnum roleId;
}
