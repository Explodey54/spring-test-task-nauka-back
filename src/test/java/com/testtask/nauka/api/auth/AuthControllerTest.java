package com.testtask.nauka.api.auth;

import com.jayway.jsonpath.JsonPath;
import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.api.auth.data.UserRepository;
import com.testtask.nauka.testUtils.DisableValidation;
import com.testtask.nauka.testUtils.EntitySaverService;
import com.testtask.nauka.testUtils.TruncateDbService;
import com.testtask.nauka.api.workers.data.WorkerRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@DisableValidation
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private EntitySaverService entitySaver;

    @AfterEach
    public void cleanUp() {
        truncateDbService.truncate();
    }

    @Test
    public void register_withMinimalData_shouldReturn200() throws Exception {
        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(getRegisterDto().toString()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void register_withMinimalData_shouldReturnIdsInDto() throws Exception {
        JSONObject jsonInput = getRegisterDto();
        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(jsonInput.toString()))
                .andExpect(jsonPathCrud("$.userId").isNumber())
                .andExpect(jsonPathCrud("$.workerId").isNumber());
    }

    @Test
    public void register_withMinimalData_shouldReturnDtoWithoutPasswordField() throws Exception {
        JSONObject jsonInput = getRegisterDto();
        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(jsonInput.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPathCrud("$.password").doesNotHaveJsonPath());
    }

    @Test
    public void register_withMinimalData_shouldCreateUserWithGuestRole() throws Exception {
        JSONObject jsonInput = getRegisterDto();
        MvcResult result = this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(jsonInput.toString()))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject outputJson = new JSONObject(result.getResponse().getContentAsString()).getJSONObject("data");
        Long userId = outputJson.getLong("userId");

        assertThat(userRepository.findById(userId)).hasValueSatisfying(i -> {
            assertThat(i.getRole()).isEqualByComparingTo(RoleEnum.GUEST);
        });
    }


    @Test
    public void register_shouldCreateLinkedUserAndWorker() throws Exception {
        JSONObject jsonInput = getRegisterDto();
        MvcResult result = this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(jsonInput.toString()))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject outputJson = new JSONObject(result.getResponse().getContentAsString()).getJSONObject("data");

        Long userId = outputJson.getLong("userId");
        Long workerId = outputJson.getLong("workerId");
        assertThat(workerRepository.findById(workerId)).isPresent();
        assertThat(userRepository.findById(userId)).hasValueSatisfying(i -> {
            assertThat(i.getWorker()).isNotNull();
            assertThat(i.getWorker().getId()).isEqualTo(workerId);
        });
    }

    @Test
    public void register_withNotUniqueUsername_shouldReturnBadRequest() throws Exception {
        entitySaver.save(new User("username1", "qwerty", RoleEnum.GUEST));

        JSONObject jsonInput = getRegisterDto();
        jsonInput.put("username", "username1");

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(jsonInput.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void register_withWrongDepartmentId_shouldReturnBadRequest() throws Exception {
        JSONObject jsonInput = getRegisterDto();
        jsonInput.put("departmentId", 99);

        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(jsonInput.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void login_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
        JSONObject loginBody = getLoginDto("username111", "qwerty112");
        this.mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON).content(loginBody.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void login_afterRegister_shouldReturnToken() throws Exception {
        JSONObject registerBody = getRegisterDto();
        registerBody.put("username", "username1234");
        registerBody.put("password", "qwerty1122");
        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(registerBody.toString()));

        JSONObject loginBody = getLoginDto("username1234", "qwerty1122");
        this.mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON).content(loginBody.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPathCrud("$.token").isNotEmpty());
    }

    @Test
    public void getUser_withValidToken_shouldReturnUserWhoHasToken() throws Exception {
        JSONObject registerBody = getRegisterDto();
        registerBody.put("username", "username1234");
        registerBody.put("password", "qwerty1122");
        this.mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(registerBody.toString()));

        JSONObject loginBody = getLoginDto("username1234", "qwerty1122");
        MvcResult loginResult = this.mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON).content(loginBody.toString()))
                .andExpect(status().isOk())
                .andReturn();
        String token = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.data.token");

        this.mockMvc.perform(get("/api/v1/auth")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPathCrud("$.username", equalTo("username1234")));
    }

    @Test
    public void getUser_withInvalidToken_shouldReturnForbidden() throws Exception {
        this.mockMvc.perform(get("/api/v1/auth")
                .header("Authorization", "Bearer wrongToken123456"))
                .andExpect(status().isForbidden());
    }

    private JSONObject getRegisterDto() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("username", "username1");
        output.put("password", "password1");
        output.put("firstName", "Petya");
        output.put("lastName", "Pupkin");
        return output;
    }

    private JSONObject getLoginDto(String login, String password) throws JSONException {
        JSONObject output = new JSONObject();
        output.put("username", login);
        output.put("password", password);
        return output;
    }
}
