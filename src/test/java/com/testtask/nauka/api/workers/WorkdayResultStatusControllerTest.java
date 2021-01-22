package com.testtask.nauka.api.workers;

import com.testtask.nauka.api.workers.data.*;
import com.testtask.nauka.testUtils.CrudAssert;
import com.testtask.nauka.testUtils.EntitySaverService;
import com.testtask.nauka.testUtils.TruncateDbService;
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

import java.util.List;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class WorkdayResultStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private WorkdayResultStatusRepository workdayResultStatusRepository;

    private List<WorkdayResultStatus> workdayResultStatuses;
    private List<WorkdayResult> workdayResults;

    private CrudAssert<WorkdayResultStatus> crudAssert;


    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, workdayResultStatusRepository, "/api/v1/workday-result-statuses"
        );
    }

    @BeforeEach
    public void refresh() {
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_shouldGetReturnAllDepartments() throws Exception {
        populateDB();
        crudAssert.assertGetAll(3);
    }

    @Test
    public void getById_shouldReturn404OnEmpty() throws Exception {
        crudAssert.assertGetById(99L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void getById_shouldReturnDepartmentById() throws Exception {
        populateDB();
        Long targetId = workdayResultStatuses.get(2).getId();
        crudAssert.assertGetById(targetId);
    }

    @Test
    public void create_shouldCreateDepartment() throws Exception {
        JSONObject inputJson = new JSONObject();
        inputJson.put("title", "Vacation");
        inputJson.put("shortTitle", "V");

        crudAssert.assertCreate(inputJson.toString(), "$.id", List.of(
                status().isCreated(),
                jsonPathCrud("$.title", equalTo("Vacation")),
                jsonPathCrud("$.shortTitle", equalTo("V"))
        ), entity -> {
            assertThat(entity.getTitle()).isEqualTo("Vacation");
            assertThat(entity.getShortTitle()).isEqualTo("V");
        });
    }

    @Test
    public void update_shouldUpdateDepartment() throws Exception {
        populateDB();
        Long targetId = workdayResultStatuses.get(2).getId();
        JSONObject inputJson = new JSONObject();
        inputJson.put("shortTitle", "U");

        crudAssert.assertUpdate(targetId, inputJson.toString(), List.of(
                status().isOk(),
                jsonPathCrud("$.shortTitle", equalTo("U")),
                jsonPathCrud("$.title", equalTo("Skipped"))
        ), entity -> {
            assertThat(entity.getShortTitle()).isEqualTo("U");
            assertThat(entity.getTitle()).isEqualTo("Skipped");
        });
    }

    @Test
    public void delete_shouldDeleteDepartment() throws Exception {
        populateDB();
        Long lastId = workdayResultStatuses.get(2).getId();

        crudAssert.assertDelete(lastId);
    }

    private void populateDB() {

        workdayResultStatuses = entitySaver.saveMultiple(List.of(
                new WorkdayResultStatus("Worked full", "WF"),
                new WorkdayResultStatus("Worked partial", "WP"),
                new WorkdayResultStatus("Skipped", "S")
        ));
        workdayResults = entitySaver.saveMultiple(List.of(
                new WorkdayResult(null, null, workdayResultStatuses.get(2))
        ));
    }
}
