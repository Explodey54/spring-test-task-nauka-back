package com.testtask.nauka.api.workers.data;

import com.fasterxml.jackson.annotation.*;
import com.testtask.nauka.api.auth.data.User;
import com.testtask.nauka.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Worker extends BaseEntity<Worker> {
    @OneToOne
    private User user;

    @ManyToOne
    private Department department;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "worker")
    private List<WorkdayResult> workdayResults;

    private String firstName;
    private String lastName;

    public Worker(Long id) {
        this.id = id;
    }

    public Worker(Department department) {
        this.department = department;
    }

    public Worker(Department department, User user) {
        this.department = department;
        this.user = user;
    }

    @Override
    public void merge(Worker source) {
        if (source.getFirstName() != null) {
            firstName = source.getFirstName();
        }
        if (source.getLastName() != null) {
            lastName = source.getLastName();
        }
        if (source.getDepartment() != null) {
            department = source.getDepartment();
        }
        if (source.getUser() != null) {
            user = source.getUser();
        }
    }
}
