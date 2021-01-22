package com.testtask.nauka.api.calendar.dto;

import com.testtask.nauka.api.calendar.data.CalendarDayStatus;
import com.testtask.nauka.common.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
@ApiModel("CalendarDayStatus")
public class CalendarDayStatusDto extends BaseDto<CalendarDayStatusDto, CalendarDayStatus> {
    public interface Create {}
    public interface Update {}

    @NotEmpty(groups = Create.class)
    private String title;
    @NotEmpty(groups = Create.class)
    private String shortTitle;
    @NotEmpty(groups = Create.class)
    @Pattern(regexp="^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$",message="Invalid hex")
    private String hexColor;
    private boolean isDefault;

    public CalendarDayStatusDto(CalendarDayStatus entity) {
        super(entity);
        this.title = entity.getTitle();
        this.shortTitle = entity.getShortTitle();
        this.hexColor = entity.getHexColor();
        this.isDefault = entity.getIsDefault();
    }

    @Override
    public CalendarDayStatus toEntity() {
        return new CalendarDayStatus(title, shortTitle, hexColor, isDefault);
    }
}
