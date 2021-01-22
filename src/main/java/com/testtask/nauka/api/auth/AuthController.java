package com.testtask.nauka.api.auth;

import com.testtask.nauka.api.auth.dto.*;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.exceptions.InvalidCredentialsException;
import com.testtask.nauka.common.response.SuccessResponse;
import com.testtask.nauka.security.JwtFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public SuccessResponse<User> getCurrentUser(HttpServletRequest request) {
        String token = JwtFilter.getTokenFromRequest(request);
        Optional<User> found = userService.getUserFromToken(token);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't find user with authorization token");
        }
        return new SuccessResponse<>(found.get());
    }

    @PostMapping
    public SuccessResponse<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        try {
            return new SuccessResponse<>(userService.login(dto));
        } catch (InvalidCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/register")
    public SuccessResponse<RegisterResponseDto> createUser(@Valid @RequestBody RegisterRequestDto dto) {
        return new SuccessResponse<>(userService.createUser(dto));
    }


}
