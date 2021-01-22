package com.testtask.nauka.api.workers.data;

import com.testtask.nauka.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.List;

import static com.testtask.nauka.common.utils.UtilHelpers.ifNotNullDo;

@Entity
@Getter @Setter
@NoArgsConstructor
public class WorkdayResultStatus extends BaseEntity<WorkdayResultStatus> {

    private String title;
    private String shortTitle;

    @OneToMany(mappedBy = "status", cascade = CascadeType.REMOVE)
    private List<WorkdayResult> workdayResults;

    public WorkdayResultStatus(Long id) {
        this.id = id;
    }

    public WorkdayResultStatus(String title, String shortTitle) {
        this.title = title;
        this.shortTitle = shortTitle;
    }

    @Override
    public void merge(WorkdayResultStatus source) {
        ifNotNullDo(source.getTitle(), this::setTitle);
        ifNotNullDo(source.getShortTitle(), this::setShortTitle);
    }
}
