package com.testtask.nauka.api.workers.data;

import com.fasterxml.jackson.annotation.*;
import com.testtask.nauka.config.LocaleConfig;
import com.testtask.nauka.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity
@Getter @Setter
@NoArgsConstructor
public class WorkdayResult extends BaseEntity<WorkdayResult> {
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;
    @Temporal(TemporalType.DATE)
    private Calendar date;
    @ManyToOne
    private WorkdayResultStatus status;

    public WorkdayResult(Worker worker, Calendar date, WorkdayResultStatus status) {
        this.worker = worker;
        this.date = date;
        this.status = status;
    }

    @Override
    public void merge(WorkdayResult source) {
        if (source.getWorker() != null) {
            this.worker = source.getWorker();
        }
        if (source.getDate() != null) {
            this.date = source.getDate();
        }
        if (source.getStatus() != null) {
            this.status = source.getStatus();
        }
    }
}
