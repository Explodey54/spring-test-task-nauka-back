package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.testUtils.CrudAssert;
import com.testtask.nauka.testUtils.DisableValidation;
import com.testtask.nauka.testUtils.EntitySaverService;
import com.testtask.nauka.testUtils.TruncateDbService;
import com.testtask.nauka.api.workers.data.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisableValidation
@WithMockUser
class WorkdayResultControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private WorkdayResultRepository workdayResultRepository;

    private List<User> users;
    private List<Worker> workers;
    private List<WorkdayResult> workdayResults;
    private List<WorkdayResultStatus> workdayResultStatuses;

    private CrudAssert<WorkdayResult> crudAssert;


    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, workdayResultRepository, "/api/v1/workday-results"
        );
    }

    @BeforeEach
    public void refresh() {
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_shouldGetReturnAllWorkdayResults() throws Exception {
        crudAssert.assertGetAll(0);
        populateDB();
        crudAssert.assertGetAll(2);
    }

    @Test
    public void getById_shouldReturn404OnEmpty() throws Exception {
        crudAssert.assertGetById(1L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void getById_shouldReturnWorkdayResultById() throws Exception {
        populateDB();
        crudAssert.assertGetById(workdayResults.get(0).getId());
    }

    @Test
    public void getById_shouldReturnWorkdayResultWithWorkerField() throws Exception {
        populateDB();
        crudAssert.assertGetById(workdayResults.get(0).getId(), List.of(
                jsonPathCrud("$.worker").exists()
        ));
    }

    @Test
    public void create_shouldCreateWorkdayResult() throws Exception {
        populateDB();
        Worker worker = workers.get(0);

        JSONObject inputJson = new JSONObject();
        inputJson.put("workerId", worker.getId());
        inputJson.put("date", "2020-11-19");
        inputJson.put("status", "WORKED");

        crudAssert.assertCreate(inputJson.toString(), "$.id");
    }

    @Test
    public void create_shouldReturnDateInISOFormat() throws Exception {
        JSONObject inputJson = new JSONObject();
        inputJson.put("date", "2020-11-19");

        crudAssert.assertCreate(inputJson.toString(), "$.id", List.of(
                jsonPathCrud("$.date", matchesRegex("2020-11-19.*"))
        ));
    }

    @Test
    public void create_withWrongWorkerId_shouldReturn400() throws Exception {
        JSONObject inputJson = new JSONObject();
        inputJson.put("workerId", 99);

        crudAssert.assertCreate(inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void update_shouldUpdateWorkdayResult() throws Exception {
        populateDB();
        WorkdayResult target = workdayResults.get(0);
        Worker newWorker = workers.get(1);

        JSONObject inputJson = new JSONObject();
        inputJson.put("workerId", newWorker.getId());
        inputJson.put("date", "2020-11-21");

        crudAssert.assertUpdate(target.getId(), inputJson.toString(), List.of(
                status().isOk(),
                jsonPathCrud("$.worker.id", equalTo(newWorker.getId().intValue())),
                jsonPathCrud("$.date", equalTo("2020-11-21"))
        ), entity -> {
            assertThat(entity.getWorker()).isNotNull();
            assertThat(entity.getWorker().getId()).isEqualTo(newWorker.getId());
            assertThat(entity.getDate()).isEqualByComparingTo(new GregorianCalendar(2020, Calendar.NOVEMBER, 21));
        });
    }

    @Test
    public void update_withWrongWorkerId_shouldReturn400() throws Exception {
        populateDB();
        WorkdayResult target = workdayResults.get(0);

        JSONObject inputJson = new JSONObject();
        inputJson.put("workerId", 99);

        crudAssert.assertUpdate(target.getId(), inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void delete_shouldDeleteWorkdayResult() throws Exception {
        populateDB();
        WorkdayResult target = workdayResults.get(0);

        crudAssert.assertDelete(target.getId());
    }

    @Test
    public void delete_onWrongId_shouldReturn404() throws Exception {
        crudAssert.assertDelete(1L, List.of(
                status().isNotFound()
        ));
    }

    // Create 3 departments
    private void populateDB() {
        users = entitySaver.saveMultiple(List.of(
                new User("user1", "123456", RoleEnum.GUEST),
                new User("user2", "123456", RoleEnum.GUEST)
        ));
        workers = entitySaver.saveMultiple(List.of(
                new Worker(null, users.get(0)),
                new Worker(null, users.get(1))
        ));
        workdayResultStatuses = entitySaver.saveMultiple(List.of(
                new WorkdayResultStatus("Worked full", "WF"),
                new WorkdayResultStatus("Worked partial", "WP")
        ));
        workdayResults = entitySaver.saveMultiple(List.of(
                new WorkdayResult(workers.get(0), Calendar.getInstance(), workdayResultStatuses.get(0)),
                new WorkdayResult(workers.get(1), Calendar.getInstance(), workdayResultStatuses.get(1))
        ));
    }
}
