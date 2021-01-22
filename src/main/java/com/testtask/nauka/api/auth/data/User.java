package com.testtask.nauka.api.auth.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testtask.nauka.common.BaseEntity;
import com.testtask.nauka.api.workers.data.Worker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity<User> {
    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Getter @Setter
    private RoleEnum role;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "user")
    @Getter @Setter
    private Worker worker;

    public User(String username, String password, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(Long id) {
        this.id = id;
    }

    @Override
    public void merge(User source) {
    }
}
