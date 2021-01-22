package com.testtask.nauka.api.workers.data;

import com.testtask.nauka.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@ToString
public class Department extends BaseEntity<Department> {
    @NotNull
    @Getter @Setter
    private String title;

    public Department(Long id) {
        this.id = id;
    }

    public Department(String title) {
        this.title = title;
    }

    public void merge(Department source) {
        if (source.getTitle() != null) { setTitle(source.getTitle()); }
    }
}
