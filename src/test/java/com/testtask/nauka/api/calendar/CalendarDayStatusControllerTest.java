package com.testtask.nauka.api.calendar;

import com.testtask.nauka.api.calendar.data.CalendarDay;
import com.testtask.nauka.api.calendar.data.CalendarDayRepository;
import com.testtask.nauka.api.calendar.data.CalendarDayStatus;
import com.testtask.nauka.api.calendar.data.CalendarDayStatusRepository;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class CalendarDayStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private CalendarDayStatusRepository calendarDayStatusRepository;
    @Autowired
    private CalendarDayRepository calendarDayRepository;

    private CrudAssert<CalendarDayStatus> crudAssert;
    private List<CalendarDayStatus> calendarDayStatuses;
    private List<CalendarDay> calendarDays;

    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, calendarDayStatusRepository, "/api/v1/calendar-day-statuses"
        );
    }

    @BeforeEach
    public void refresh() {
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_shouldReturnAllStatuses() throws Exception {
        populateDB();
        crudAssert.assertGetAll(3);
    }

    @Test
    public void getById_shouldReturnStatus() throws Exception {
        populateDB();
        Long targetId = calendarDayStatuses.get(1).getId();
        crudAssert.assertGetById(targetId);
    }

    @Test
    public void getById_withWrongId_shouldReturn404() throws Exception {
        populateDB();
        crudAssert.assertGetById(99L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void create_shouldCreateStatus() throws Exception {
        populateDB();
        JSONObject rawJson = new JSONObject();
        rawJson.put("title", "Skipped work");
        rawJson.put("shortTitle", "SW");
        rawJson.put("hexColor", "#aa2aa2");

        crudAssert.assertCreate(rawJson.toString(), "$.id");
    }

    @Test
    public void create_withDefaultFlagEqualsTrue_shouldCreateStatus_andSetDefaultToFalseForOthers() throws Exception {
        populateDB();
        JSONObject rawJson = new JSONObject();
        rawJson.put("title", "Skipped work");
        rawJson.put("shortTitle", "SW");
        rawJson.put("hexColor", "#aa2aa2");
        rawJson.put("default", true);

        crudAssert.assertCreate(rawJson.toString(), "$.id");

        List<CalendarDayStatus> found = calendarDayStatusRepository.findAll().stream()
                .filter(CalendarDayStatus::getIsDefault)
                .collect(Collectors.toList());

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Skipped work");
    }

    @Test
    public void update_shouldUpdateStatus() throws Exception {
        populateDB();
        Long targetId = calendarDayStatuses.get(0).getId();
        JSONObject rawJson = new JSONObject();
        rawJson.put("title", "Worked fully");
        rawJson.put("shortTitle", "WF_2");
        rawJson.put("hexColor", "#aaabbb");

        crudAssert.assertUpdate(targetId, rawJson.toString());
    }

    @Test
    public void update_withDefaultFlagEqualsTrue_shouldUpdateStatus_andSetDefaultToFalseForOthers() throws Exception {
        populateDB();
        Long targetId = calendarDayStatuses.get(2).getId();
        JSONObject rawJson = new JSONObject();
        rawJson.put("default", true);

        crudAssert.assertUpdate(targetId, rawJson.toString(), List.of(
                status().isOk(),
                jsonPathCrud("$.default").value(true)
        ));

        List<CalendarDayStatus> found = calendarDayStatusRepository.findAll().stream()
                .filter(CalendarDayStatus::getIsDefault)
                .collect(Collectors.toList());

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Holiday");
    }

    @Test
    public void delete_shouldDeleteStatus() throws Exception {
        populateDB();
        Long targetId = calendarDayStatuses.get(0).getId();

        crudAssert.assertDelete(targetId);
    }

    @Test
    public void delete_shouldCascadeDeleteLinkedCalendarDays() throws Exception {
        populateDB();
        Long targetId = calendarDayStatuses.get(2).getId();

        crudAssert.assertDelete(targetId);

        assertThat(calendarDayRepository.findAll()).isEmpty();
    }

    private void populateDB() {
        calendarDayStatuses = entitySaver.saveMultiple(List.of(
                new CalendarDayStatus("Worked full", "WF", "#aaa", true),
                new CalendarDayStatus("Weekend rest", "WR", "#c0e0e0"),
                new CalendarDayStatus("Holiday", "H", "#baeae0")
        ));
        calendarDays = entitySaver.saveMultiple(List.of(
                new CalendarDay(new GregorianCalendar(2019, Calendar.FEBRUARY, 25), calendarDayStatuses.get(2))
        ));
    }
}
