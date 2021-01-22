package com.testtask.nauka.api.auth;

import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.data.UserRepository;
import com.testtask.nauka.testUtils.CrudAssert;
import com.testtask.nauka.testUtils.DisableValidation;
import com.testtask.nauka.testUtils.EntitySaverService;
import com.testtask.nauka.testUtils.TruncateDbService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisableValidation
@WithMockUser
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private UserRepository userRepository;

    private CrudAssert<User> crudAssert;
    private List<User> users;


    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, userRepository,"/api/v1/users"
        );
    }

    @BeforeEach
    public void refresh() {
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_shouldGetReturnAll() throws Exception {
        crudAssert.assertGetAll(0);
        populateDB();
        crudAssert.assertGetAll(2);
    }

    @Test
    public void getById_shouldReturnById() throws Exception {
        populateDB();
        Long targetId = users.get(1).getId();
        crudAssert.assertGetById(targetId, List.of(
                status().isOk(),
                jsonPathCrud("$.username", equalTo("user2")),
                jsonPathCrud("$.role", equalTo("GUEST")),
                jsonPathCrud("$.password").doesNotExist()
        ));
    }

    @Test
    public void getById_onWrongId_shouldReturnNotFound() throws Exception {
        crudAssert.assertGetById(1L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void delete_shouldDelete() throws Exception {
        populateDB();
        Long targetId = users.get(1).getId();

        crudAssert.assertDelete(targetId);
    }

    @Test
    public void delete_onWrongId_shouldReturn404() throws Exception {
        crudAssert.assertDelete(1L, List.of(
                status().isNotFound()
        ));
    }

    private void populateDB() {
        users = entitySaver.saveMultiple(List.of(
                new User("user1", "qwerty", RoleEnum.GUEST),
                new User("user2", "qwerty", RoleEnum.GUEST)
        ));
    }
}
