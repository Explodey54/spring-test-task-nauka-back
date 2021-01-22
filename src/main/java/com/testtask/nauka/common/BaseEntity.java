package com.testtask.nauka.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
abstract public class BaseEntity<T extends BaseEntity<T>> {
    @Id
    @GeneratedValue
    @Getter @Setter
    protected Long id;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public abstract void merge(T source);
}
