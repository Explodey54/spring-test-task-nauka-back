package com.testtask.nauka.api.calendar.data;

import com.testtask.nauka.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
public class CalendarDayStatus extends BaseEntity<CalendarDayStatus> {

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String shortTitle;

    @Getter @Setter
    private String hexColor;

    @Getter @Setter
    private Boolean isDefault = false;

    @OneToMany(mappedBy = "status", cascade = CascadeType.REMOVE)
    @Getter @Setter
    private List<CalendarDay> calendarDays;

    public CalendarDayStatus(Long id) {
        this.id = id;
    }

    public CalendarDayStatus(String title, String shortTitle, String hexColor) {
        this(title, shortTitle, hexColor, false);
    }

    public CalendarDayStatus(String title, String shortTitle, String hexColor, boolean isDefault) {
        this.title = title;
        this.shortTitle = shortTitle;
        this.hexColor = hexColor;
        this.isDefault = isDefault;
    }

    @Override
    public void merge(CalendarDayStatus source) {
        if (source.getTitle() != null) {
            this.title = source.getTitle();
        }
        if (source.getShortTitle() != null) {
            this.shortTitle = source.getShortTitle();
        }
        if (source.getHexColor() != null) {
            this.hexColor = source.getHexColor();
        }
        if (source.getIsDefault() != null) {
            this.isDefault = source.getIsDefault();
        }
    }
}
