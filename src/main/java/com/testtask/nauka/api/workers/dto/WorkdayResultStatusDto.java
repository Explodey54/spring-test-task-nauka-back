package com.testtask.nauka.api.workers.dto;

import com.testtask.nauka.api.workers.data.WorkdayResultStatus;
import com.testtask.nauka.common.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter @Setter
@NoArgsConstructor
@ApiModel("WorkdayResultStatus")
public class WorkdayResultStatusDto extends BaseDto<WorkdayResultStatusDto, WorkdayResultStatus> {
    public interface Create {}

    @NotEmpty(groups = Create.class)
    private String title;
    @NotEmpty(groups = Create.class)
    private String shortTitle;

    public WorkdayResultStatusDto(WorkdayResultStatus entity) {
        super(entity);
        this.title = entity.getTitle();
        this.shortTitle = entity.getShortTitle();
    }

    @Override
    public WorkdayResultStatus toEntity() {
        return new WorkdayResultStatus(title, shortTitle);
    }
}
