package com.testtask.nauka.api.auth;

import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.data.UserRepository;
import com.testtask.nauka.api.auth.dto.RegisterRequestDto;
import com.testtask.nauka.api.auth.dto.RegisterResponseDto;
import com.testtask.nauka.testUtils.TruncateDbService;
import com.testtask.nauka.api.workers.data.Worker;
import com.testtask.nauka.api.workers.data.WorkerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Autowired
    private TruncateDbService truncateDbService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @AfterEach
    public void cleanUp() {
        truncateDbService.truncate();
    }

    @Test
    public void createUser_shouldReturnDtoWithUserId() throws Exception {
        RegisterRequestDto dto = buildRegisterDto();
        RegisterResponseDto output = userService.createUser(dto);
        assertNotNull(output);
        assertNotNull(output.getUserId());
    }

    @Test
    public void createUser_shouldSetRoleToGUEST() throws Exception {
        RegisterRequestDto dto = buildRegisterDto();

        RegisterResponseDto output = userService.createUser(dto);

        User createdUser = userRepository.findById(output.getUserId()).get();
        assertEquals(createdUser.getRole(), RoleEnum.GUEST);
    }

    @Test
    public void createUser_shouldCreateUserAndWorker() throws Exception {
        RegisterRequestDto dto = buildRegisterDto();

        RegisterResponseDto output = userService.createUser(dto);

        Optional<User> foundUser = userRepository.findById(output.getUserId());
        Optional<Worker> foundWorker = workerRepository.findById(output.getWorkerId());

        assertThat(foundUser).isPresent();
        assertThat(foundWorker).isPresent();
    }


    private RegisterRequestDto buildRegisterDto() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setUsername("username1");
        dto.setPassword("qwerty1");
        return dto;
    }
}
