package com.testtask.nauka.api.auth;

import com.testtask.nauka.api.auth.data.*;
import com.testtask.nauka.api.auth.dto.*;
import com.testtask.nauka.api.auth.exceptions.InvalidCredentialsException;
import com.testtask.nauka.api.workers.data.Worker;
import com.testtask.nauka.common.exceptions.RelationNotFoundException;
import com.testtask.nauka.common.exceptions.UniqueFieldDuplicationException;
import com.testtask.nauka.common.utils.PasswordEncoderProvider;
import com.testtask.nauka.common.AbstractCrudService;
import com.testtask.nauka.api.workers.data.Department;
import com.testtask.nauka.api.workers.data.DepartmentRepository;
import com.testtask.nauka.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends AbstractCrudService<User, Long> implements UserDetailsService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoderProvider encoderProvider;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserService(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            PasswordEncoderProvider encoderProvider,
            JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.encoderProvider = encoderProvider;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return new UserPrincipal(found.get());
    }

    public RegisterResponseDto createUser(RegisterRequestDto dto) {
        User user = createUserFromRegisterRequest(dto);
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UniqueFieldDuplicationException("Username already exists", "username");
        }
        if (dto.getDepartmentId() != null) {
            Optional<Department> foundDepartment = departmentRepository.findById(dto.getDepartmentId());
            foundDepartment.ifPresentOrElse(i -> user.getWorker().setDepartment(i), () -> {
                throw new RelationNotFoundException("Department not found");
            });
        }
        user.setRole(RoleEnum.GUEST);
        user.setPassword(encoderProvider.getEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        RegisterResponseDto output = new RegisterResponseDto();
        output.setUserId(savedUser.getId());
        return new RegisterResponseDto(user, user.getWorker(), user.getWorker().getDepartment());
    }

    public LoginResponseDto login(LoginRequestDto dto) throws InvalidCredentialsException {
        Optional<User> found = userRepository.findByUsername(dto.getUsername());
        if (found.isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        PasswordEncoder encoder = encoderProvider.getEncoder();
        boolean isPasswordCorrect = encoder.matches(dto.getPassword(), found.get().getPassword());
        if (!isPasswordCorrect) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return new LoginResponseDto(jwtProvider.generateToken(found.get().getUsername()));
    }

    public Optional<User> getUserFromToken(String token) {
        String username = jwtProvider.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        String adminUsername = "admin";
        String adminPassword = "qwerty";
        Optional<User> found = userRepository.findByUsername(adminUsername);
        if (found.isEmpty()) {
            User user = new User(adminUsername, adminPassword, RoleEnum.ADMIN);
            user.setPassword(encoderProvider.getEncoder().encode(user.getPassword()));
            userRepository.save(user);
            System.out.println("Admin user created");
        }
    }

    private User createUserFromRegisterRequest(RegisterRequestDto dto) {
        User user = new User();
        Worker worker = new Worker();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        worker.setFirstName(dto.getFirstName());
        worker.setLastName(dto.getLastName());
        user.setWorker(worker);

        return user;
    }
}
