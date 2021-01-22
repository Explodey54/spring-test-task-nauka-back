package com.testtask.nauka.api.auth;

import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.dto.UserDto;
import com.testtask.nauka.api.auth.data.UserRepository;
import com.testtask.nauka.common.AbstractCrudController;
import com.testtask.nauka.common.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends AbstractCrudController<User, UserDto> {

    @Autowired
    public UserController(UserRepository userRepository) {
        super(userRepository, User.class, UserDto.class);
    }

    @GetMapping
    public SuccessResponse<List<UserDto>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public SuccessResponse<UserDto> getById(@PathVariable Long id) {
        return super.getById(id);
    }

//    @PostMapping
//    public UserDto create(User input) {
//        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "User creation via /users is not allowed");
//    }
//
//    @PutMapping("/{id}")
//    public UserDto update(User input, @PathVariable Long id) {
//        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "User editing via /users is not allowed");
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        super.delete(id);
    }
}
