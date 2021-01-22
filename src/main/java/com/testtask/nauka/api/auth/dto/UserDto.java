package com.testtask.nauka.api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.common.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@ApiModel("User")
public class UserDto extends BaseDto<UserDto, User> {
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private RoleEnum role;

    public UserDto(User entity) {
        super(entity);
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.role = entity.getRole();
    }

    @Override
    public User toEntity() {
        return new User(username, password, role);
    }
}
