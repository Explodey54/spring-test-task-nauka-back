package com.testtask.nauka.api.workers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.testtask.nauka.api.workers.data.WorkdayResult;
import com.testtask.nauka.api.workers.data.WorkdayResultStatus;
import com.testtask.nauka.api.workers.data.Worker;
import com.testtask.nauka.common.BaseDto;
import com.testtask.nauka.config.LocaleConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Calendar;

import static com.testtask.nauka.common.utils.UtilHelpers.ifNotNull;

@Getter @Setter
@NoArgsConstructor
@ApiModel("WorkdayResult")
public class WorkdayResultDto extends BaseDto<WorkdayResultDto, WorkdayResult> {
    public interface Create {}

    @JsonFormat(pattern = LocaleConfig.DATE_FORMAT)
    private Calendar date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WorkdayResultStatusDto status;

    @NotNull(groups = Create.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long statusId;

    @JsonIgnoreProperties({"workdayResults", "user", "department"})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WorkerDto worker;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long workerId;

    public WorkdayResultDto(WorkdayResult entity) {
        super(entity);
        date = entity.getDate();
        status = ifNotNull(entity.getStatus(), WorkdayResultStatusDto::new);
        Worker workerRaw = entity.getWorker();
        if (workerRaw != null) {
            worker = new WorkerDto(workerRaw, workerRaw.getDepartment(), null, null);
            workerId = workerRaw.getId();
        }

    }

    @Override
    public WorkdayResult toEntity() {
        Worker worker = ifNotNull(workerId, Worker::new);
        return new WorkdayResult(worker, date, ifNotNull(statusId, WorkdayResultStatus::new));
    }
}
