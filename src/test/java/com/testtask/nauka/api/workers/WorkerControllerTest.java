package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.auth.data.RoleEnum;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.common.utils.DateTimeService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisableValidation
@WithMockUser
class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private WorkerRepository workerRepository;
    @MockBean
    private DateTimeService dateTimeService;

    private final Calendar CALENDAR_BEFORE = new GregorianCalendar(2020, Calendar.FEBRUARY, 1);
    private final Calendar CALENDAR_AFTER = new GregorianCalendar(2020, Calendar.JUNE, 1);

    private CrudAssert<Worker> crudAssert;
    private List<User> users;
    private List<Worker> workers;
    private List<Department> departments;
    private List<WorkdayResult> workdayResults;


    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, workerRepository, "/api/v1/workers"
        );
    }

    @BeforeEach
    public void refresh() {
        when(dateTimeService.getTodayCalendar()).then(i -> getToday());
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_onEmptyWorkers_shouldReturnEmptyList() throws Exception {
        crudAssert.assertGetAll(0);
    }

    @Test
    public void getAll_withoutParams_shouldReturnAllWorkersWithWorkdayInCurrentMonth() throws Exception {
        populateDB();
        crudAssert.assertGetAll(List.of(
                status().isOk(),
                jsonPathCrud("$", hasSize(3)),
                jsonPathCrud("$[0].workdayResults", hasSize(1)),
                jsonPathCrud("$[1].workdayResults", hasSize(1)),
                jsonPathCrud("$[2].workdayResults", hasSize(0))
        ));
    }

    @Test
    public void getAll_withDepartment_shouldReturnWorkersWithinDepartmentWithWorkdayInCurrentMonth() throws Exception {
        populateDB();
        Long departmentId = departments.get(1).getId();
        crudAssert.addQueryArg("department", departmentId);
        crudAssert.assertGetAll(List.of(
                status().isOk(),
                jsonPathCrud("$", hasSize(2)),
                jsonPathCrud("$[0].workdayResults", hasSize(1)),
                jsonPathCrud("$[1].workdayResults", hasSize(0))
        ));
    }

    @Test
    public void getAll_withMonth_shouldReturnAllWorkersWithWorkdayInTargetMonth() throws Exception {
        populateDB();
        crudAssert.addQueryArg("workday_month", 2);
        crudAssert.assertGetAll(List.of(
                status().isOk(),
                jsonPathCrud("$", hasSize(3)),
                jsonPathCrud("$[0].workdayResults", hasSize(0)),
                jsonPathCrud("$[1].workdayResults", hasSize(1)),
                jsonPathCrud("$[2].workdayResults", hasSize(0))
        ));
    }

    @Test
    public void getAll_withMonthAndDepartment_shouldReturnWorkersWithinDepartmentWithWorkdayInTargetMonth() throws Exception {
        populateDB();
        Long departmentId = departments.get(1).getId();
        crudAssert.addQueryArg("department", departmentId);
        crudAssert.addQueryArg("workday_month", 6);
        crudAssert.assertGetAll(List.of(
                status().isOk(),
                jsonPathCrud("$", hasSize(2)),
                jsonPathCrud("$[0].workdayResults", hasSize(0)),
                jsonPathCrud("$[1].workdayResults", hasSize(1))
        ));
    }

    @Test
    public void getById_OnEmpty_shouldReturn404() throws Exception {
        crudAssert.assertGetById(1L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void getById_shouldReturnWorkerById() throws Exception {
        populateDB();
        Worker last = workers.get(departments.size() - 1);
        crudAssert.assertGetById(last.getId());
    }

    @Test
    public void create_withoutRelations_shouldCreateWorker() throws Exception {
        JSONObject json = new JSONObject();
        json.put("firstName", "John");
        json.put("lastName", "Doe");
        crudAssert.assertCreate(json.toString(), "$.id", List.of(
                status().isCreated(),
                jsonPathCrud("$.firstName", equalTo("John")),
                jsonPathCrud("$.lastName", equalTo("Doe"))
        ), entity -> {
            assertThat(entity.getFirstName()).isEqualTo("John");
            assertThat(entity.getLastName()).isEqualTo("Doe");
        });
    }

    @Test
    public void create_withDepartmentId_shouldCreateWorkerWithDepartment() throws Exception {
        populateDB();
        Department department = departments.get(0);
        JSONObject inputJson = new JSONObject();
        inputJson.put("departmentId", department.getId());

        crudAssert.assertCreate(inputJson.toString(), "$.id", List.of(
                status().isCreated(),
                jsonPathCrud("$.department").exists(),
                jsonPathCrud("$.departmentId").doesNotExist()
        ), entity -> {
            assertThat(entity.getDepartment()).isNotNull();
            assertThat(entity.getDepartment().getId()).isEqualTo(department.getId());
            assertThat(entity.getDepartment().getTitle()).isEqualTo("test dep 1");
        });
    }

    @Test
    public void create_withUserId_shouldCreateWorkerWithUser() throws Exception {
        populateDB();
        User user = users.get(1);
        JSONObject inputJson = new JSONObject();
        inputJson.put("userId", user.getId());

        crudAssert.assertCreate(inputJson.toString(), "$.id", List.of(
                status().isCreated(),
                jsonPathCrud("$.user").exists(),
                jsonPathCrud("$.user.id", equalTo(user.getId().intValue())),
                jsonPathCrud("$.user.username", equalTo("user2")),
                jsonPathCrud("$.userId").doesNotExist()
        ), entity -> {
            assertThat(entity.getUser()).isNotNull();
            assertThat(entity.getUser().getId()).isEqualTo(user.getId());
            assertThat(entity.getUser().getUsername()).isEqualTo("user2");
        });
    }

    @Test
    public void create_withWrongDepartmentId_shouldReturn400() throws Exception {
        JSONObject inputJson = new JSONObject();
        inputJson.put("departmentId", 99);

        crudAssert.assertCreate(inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void create_withWrongUserId_shouldReturn400() throws Exception {
        populateDB();

        JSONObject inputJson = new JSONObject();
        inputJson.put("userId", 999);

        crudAssert.assertCreate(inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void create_withUserThatHasWorkerAlready_shouldReturn400() throws Exception {
        populateDB();
        User user = users.get(0);

        JSONObject inputJson = new JSONObject();
        inputJson.put("userId", user.getId());

        crudAssert.assertCreate(inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void update_shouldUpdateWorker() throws Exception {
        populateDB();
        Worker targetWorker = workers.get(1);
        Department newDepartment = departments.get(0);
        User newUser = users.get(1);

        JSONObject inputJson = new JSONObject();
        inputJson.put("firstName", "John123");
        inputJson.put("lastName", "Doe123");
        inputJson.put("departmentId", newDepartment.getId());
        inputJson.put("userId", newUser.getId());

        crudAssert.assertUpdate(targetWorker.getId(), inputJson.toString(), List.of(
                status().isOk(),
                jsonPathCrud("$.firstName", equalTo("John123")),
                jsonPathCrud("$.lastName", equalTo("Doe123")),
                jsonPathCrud("$.departmentId").doesNotExist(),
                jsonPathCrud("$.department.id", equalTo(newDepartment.getId().intValue())),
                jsonPathCrud("$.department.title", equalTo("test dep 1")),
                jsonPathCrud("$.userId").doesNotExist(),
                jsonPathCrud("$.user.id", equalTo(newUser.getId().intValue())),
                jsonPathCrud("$.user.username", equalTo("user2"))
        ), entity -> {
            assertThat(entity.getFirstName()).isEqualTo("John123");
            assertThat(entity.getLastName()).isEqualTo("Doe123");
            assertThat(entity.getUser()).isNotNull();
            assertThat(entity.getUser().getId()).isEqualTo(newUser.getId());
        });
    }

    @Test
    public void update_withWrongDepartmentId_shouldReturn400() throws Exception {
        populateDB();
        Worker targetWorker = workers.get(0);

        JSONObject inputJson = new JSONObject();
        inputJson.put("departmentId", 999);

        crudAssert.assertUpdate(targetWorker.getId(), inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void update_withWrongUserId_shouldReturn400() throws Exception {
        populateDB();
        Worker targetWorker = workers.get(0);

        JSONObject inputJson = new JSONObject();
        inputJson.put("userId", 999);

        crudAssert.assertUpdate(targetWorker.getId(), inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void update_withUserThatHasWorkerAlready_shouldReturn400() throws Exception {
        populateDB();
        Worker targetWorker = workers.get(0);
        User newUser = users.get(0);

        JSONObject inputJson = new JSONObject();
        inputJson.put("userId", newUser.getId());

        crudAssert.assertUpdate(targetWorker.getId(), inputJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    // department1,2
    // worker1 - department1, worker2,3 - department2
    // workday1 (today) - worker1, workday2 (before) - worker2
    // workday3 (today) - worker2, workday4 (after) - worker3
    private void populateDB() {
        users = entitySaver.saveMultiple(List.of(
                new User("user1", "qwerty", RoleEnum.GUEST),
                new User("user2", "qwerty123", RoleEnum.GUEST)
        ));
        departments = entitySaver.saveMultiple(List.of(
                new Department("test dep 1"),
                new Department("test dep 2")
        ));
        workers = entitySaver.saveMultiple(List.of(
                new Worker(departments.get(0)),
                new Worker(departments.get(1), users.get(0)),
                new Worker(departments.get(1))
        ));
        workdayResults = entitySaver.saveMultiple(List.of(
                new WorkdayResult(workers.get(0), getToday(), null),
                new WorkdayResult(workers.get(1), CALENDAR_BEFORE, null),
                new WorkdayResult(workers.get(1), getToday(), null),
                new WorkdayResult(workers.get(2), CALENDAR_AFTER, null)
        ));
    }

    private Calendar getToday() {
        return new GregorianCalendar(2020, Calendar.MARCH, 1);
    }
}
