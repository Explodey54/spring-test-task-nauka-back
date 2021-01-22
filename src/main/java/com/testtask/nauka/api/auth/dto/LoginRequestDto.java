package com.testtask.nauka.api.auth.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("LoginRequest")
public class LoginRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
