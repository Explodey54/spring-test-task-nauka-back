package com.testtask.nauka.api.calendar.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.testtask.nauka.config.LocaleConfig;
import com.testtask.nauka.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@NoArgsConstructor
public class CalendarDay extends BaseEntity<CalendarDay> {
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = LocaleConfig.DATE_FORMAT)
    @Getter @Setter
    private Calendar date;

    @ManyToOne
    @JoinColumn(name="status_id")
    @Getter @Setter
    private CalendarDayStatus status;

    public CalendarDay(Calendar date, CalendarDayStatus status) {
        this.date = date;
        this.status = status;
    }

    @Override
    public void merge(CalendarDay source) {
        if (source.getDate() != null) {
            this.date = source.getDate();
        }
        if (source.getStatus() != null) {
            this.status = source.getStatus();
        }
    }
}
