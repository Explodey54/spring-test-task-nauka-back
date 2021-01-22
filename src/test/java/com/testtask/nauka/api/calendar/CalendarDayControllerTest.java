package com.testtask.nauka.api.calendar;

import com.testtask.nauka.api.calendar.data.CalendarDay;
import com.testtask.nauka.api.calendar.data.CalendarDayRepository;
import com.testtask.nauka.api.calendar.data.CalendarDayStatus;
import com.testtask.nauka.common.utils.DateTimeService;
import com.testtask.nauka.testUtils.CrudAssert;
import com.testtask.nauka.testUtils.DisableValidation;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.testtask.nauka.testUtils.CrudAssertResultMatchers.jsonPathCrud;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
public class CalendarDayControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TruncateDbService truncateDbService;
    @Autowired
    private EntitySaverService entitySaver;
    @Autowired
    private CalendarDayRepository repository;
    @MockBean
    private DateTimeService dateTimeService;

    private CrudAssert<CalendarDay> crudAssert;
    private List<CalendarDay> calendarDays;
    private List<CalendarDayStatus> calendarDayStatuses;

    private final Calendar TODAY = new GregorianCalendar(2020, Calendar.MARCH, 15);

    @BeforeAll
    public void setUp() {
        crudAssert = new CrudAssert<>(
                mockMvc, repository, "/api/v1/calendar-days"
        );
    }

    @BeforeEach
    public void refresh() {
        when(dateTimeService.getTodayCalendar()).then(i -> getToday(TODAY));
        truncateDbService.truncate();
        crudAssert.reset();
    }

    @Test
    public void getAll_shouldReturnCalendarDayList_InTodayYear_InTodayMonth() throws Exception {
        populateDB();
        crudAssert.assertGetAll(2);
    }

    @Test
    public void getAll_withYear_shouldGetReturnCalendarDayList_InTargetYear_InTodayMonth() throws Exception {
        populateDB();
        crudAssert.addQueryArg("year", 2021);
        crudAssert.assertGetAll(0);

        crudAssert.reset();

        crudAssert.addQueryArg("year", 2019);
        crudAssert.assertGetAll(1);
    }

    @Test
    public void getAll_withMonthOutOfBounds_shouldReturnBadRequest() throws Exception {
        populateDB();
        crudAssert.addQueryArg("month", 0);
        crudAssert.assertGetAll(List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void getAll_withMonth_shouldReturnCalendarDayList_InTargetMonth_InTodayYear() throws Exception {
        populateDB();
        crudAssert.addQueryArg("month", 1);
        crudAssert.assertGetAll(1);

        crudAssert.reset();

        crudAssert.addQueryArg("month", 2);
        crudAssert.assertGetAll(0);
    }

    @Test
    public void getById_shouldReturnById() throws Exception {
        populateDB();
        Long targetId = calendarDays.get(0).getId();

        crudAssert.assertGetById(targetId);
    }

    @Test
    public void getById_shouldReturnDateInISOFormat() throws Exception {
        populateDB();
        Long targetId = calendarDays.get(0).getId();

        crudAssert.assertGetById(targetId, List.of(
                jsonPathCrud("$.date", equalTo("2019-03-05"))
        ));
    }

    @Test
    public void getById_onWrongId_shouldReturn404() throws Exception {
        crudAssert.assertGetById(1L, List.of(
                status().isNotFound()
        ));
    }

    @Test
    public void create_shouldCreateCalendarDay() throws Exception {
        populateDB();
        Long statusId = calendarDayStatuses.get(0).getId();
        JSONObject rawJson = new JSONObject();
        rawJson.put("date", "2020-03-21");
        rawJson.put("statusId", statusId);

        crudAssert.assertCreate(rawJson.toString(), "$.id");
    }

    @Test
    public void create_withNotUniqueDate_shouldReturnBadRequest() throws Exception {
        populateDB();
        Long statusId = calendarDayStatuses.get(0).getId();
        JSONObject rawJson = new JSONObject();
        rawJson.put("date", "2020-01-01");
        rawJson.put("statusId", statusId);

        crudAssert.assertCreate(rawJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void update_shouldUpdateCalendarDay() throws Exception {
        populateDB();
        Long targetId = calendarDays.get(0).getId();
        Long newStatusId = calendarDayStatuses.get(1).getId();
        JSONObject rawJson = new JSONObject();
        rawJson.put("date", "2020-02-02");
        rawJson.put("statusId", newStatusId);

        crudAssert.assertUpdate(targetId, rawJson.toString(), List.of(
                status().isOk(),
                jsonPathCrud("$.date", equalTo("2020-02-02")),
                jsonPathCrud("$.status.id", equalTo(newStatusId.intValue()))
        ), entity -> {
            assertThat(entity.getDate()).isEqualByComparingTo(new GregorianCalendar(2020, Calendar.FEBRUARY, 2));
            assertThat(entity.getStatus().getId()).isEqualTo(newStatusId);
        });
    }

    @Test
    public void update_withNotUniqueDate_shouldReturnBadRequest() throws Exception {
        populateDB();
        Long targetId = calendarDays.get(1).getId();
        JSONObject rawJson = new JSONObject();
        rawJson.put("date", "2019-03-05");

        crudAssert.assertUpdate(targetId, rawJson.toString(), List.of(
                status().isBadRequest()
        ));
    }

    @Test
    public void delete_shouldDeleteCalendarDay() throws Exception {
        populateDB();
        Long targetId = calendarDays.get(0).getId();
        crudAssert.assertDelete(targetId);
    }

    @Test
    public void delete_onWrongId_shouldReturn404() throws Exception {
        crudAssert.assertDelete(1L, List.of(
                status().isNotFound()
        ));
    }

    private void populateDB() {
        calendarDayStatuses = entitySaver.saveMultiple(List.of(
                new CalendarDayStatus("Worked full", "WF", "#aaa"),
                new CalendarDayStatus("Weekend rest", "WR", "#c0e0e0")
        ));
        calendarDays = entitySaver.saveMultiple(List.of(
                new CalendarDay(new GregorianCalendar(2019, Calendar.MARCH, 5), calendarDayStatuses.get(0)),
                new CalendarDay(new GregorianCalendar(2020, Calendar.JANUARY, 1), calendarDayStatuses.get(0)),
                new CalendarDay(new GregorianCalendar(2020, Calendar.MARCH, 10), calendarDayStatuses.get(1)),
                new CalendarDay(new GregorianCalendar(2020, Calendar.MARCH, 14), calendarDayStatuses.get(1))
        ));
    }

    private Calendar getToday(Calendar input) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(input.getTimeInMillis());
        return calendar;
    }
}
