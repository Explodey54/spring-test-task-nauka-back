package com.testtask.nauka.api.workers;

import com.testtask.nauka.testUtils.*;
import com.testtask.nauka.api.workers.data.Department;
import com.testtask.nauka.api.workers.data.DepartmentRepository;
import org.json.JSONException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisableValidation
@WithMockUser
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private DepartmentRepository departmentRepository;

    private final String DEPARTMENT_STUB_JSON =
        "{" +
            "title: \"Title 1\"" +
        "}";

    private List<Department> departments;
    private CrudAssert<Department> crudAssert;


    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, departmentRepository, "/api/v1/departments"
        );
    }

    @BeforeEach
    public void refresh() {
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_shouldGetReturnAllDepartments() throws Exception {
        crudAssert.assertGetAll(0);
        populateDB();
        crudAssert.assertGetAll(3);
    }

    @Test
    public void getById_shouldReturn404OnEmpty() throws Exception {
        crudAssert.assertGetById(1L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void getById_shouldReturnDepartmentById() throws Exception {
        populateDB();
        Department last = departments.get(departments.size() - 1);
        crudAssert.assertGetById(last.getId());
    }

    @Test
    public void create_shouldCreateDepartment() throws Exception {
        JSONObject inputJson = getDepartmentJson();
        crudAssert.assertCreate(inputJson.toString(), "$.id", List.of(
                status().isCreated(),
                jsonPathCrud("$.title", equalTo("Title 1"))
        ), entity -> {
            assertThat(entity.getTitle()).isEqualTo("Title 1");
        });
    }

    @Test
    public void update_shouldUpdateDepartment() throws Exception {
        populateDB();
        Long lastId = departments.get(departments.size() - 1).getId();
        JSONObject inputJson = getDepartmentJson();
        inputJson.put("title", "updated title");

        crudAssert.assertUpdate(lastId, inputJson.toString(), List.of(
                status().isOk(),
                jsonPathCrud("$.title", equalTo("updated title"))
        ), entity -> {
            assertThat(entity.getTitle()).isEqualTo("updated title");
        });
    }

    @Test
    public void delete_shouldDeleteDepartment() throws Exception {
        populateDB();
        Long lastId = departments.get(departments.size() - 1).getId();

        crudAssert.assertDelete(lastId);
    }

    @Test
    public void delete_onWrongId_shouldReturn404() throws Exception {
        crudAssert.assertDelete(1L, List.of(
                status().isNotFound()
        ));
    }

    // Create 3 departments
    private void populateDB() {
        departments = entitySaver.saveMultiple(List.of(
                new Department(),
                new Department("title2"),
                new Department("title3")
        ));
    }

    private JSONObject getDepartmentJson() throws JSONException {
        return new JSONObject(DEPARTMENT_STUB_JSON);
    }
}