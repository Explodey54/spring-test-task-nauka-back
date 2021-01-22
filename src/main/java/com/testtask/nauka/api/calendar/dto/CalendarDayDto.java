package com.testtask.nauka.api.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.testtask.nauka.api.calendar.data.CalendarDay;
import com.testtask.nauka.api.calendar.data.CalendarDayStatus;
import com.testtask.nauka.common.BaseDto;
import com.testtask.nauka.config.LocaleConfig;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Calendar;

import static com.testtask.nauka.common.utils.UtilHelpers.ifNotNull;

@Getter @Setter
@NoArgsConstructor
@ApiModel("CalendarDay")
public class CalendarDayDto extends BaseDto<CalendarDayDto, CalendarDay> {
    public interface Create {}
    public interface Update {}

    @NotNull(groups = Create.class)
    @JsonFormat(pattern = LocaleConfig.DATE_FORMAT)
    private Calendar date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CalendarDayStatusDto status;

    @NotNull(groups = Create.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long statusId;

    public CalendarDayDto(CalendarDay entity) {
        super(entity);
        this.date = entity.getDate();
        this.status = ifNotNull(entity.getStatus(), CalendarDayStatusDto::new);
    }

    @Override
    public CalendarDay toEntity() {
        return new CalendarDay(date, ifNotNull(statusId, CalendarDayStatus::new));
    }
}
