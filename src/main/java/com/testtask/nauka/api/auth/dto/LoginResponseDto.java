package com.testtask.nauka.api.auth.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("LoginResponse")
public class LoginResponseDto {
    private String token;

    public LoginResponseDto(String token) {
        this.token = token;
    }
}
