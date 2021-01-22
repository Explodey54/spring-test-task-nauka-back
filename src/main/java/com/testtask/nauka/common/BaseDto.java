package com.testtask.nauka.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
abstract public class BaseDto<SELF extends BaseDto<SELF, E>, E extends BaseEntity<E>> {
    @Getter @Setter
    private Long id;

    public BaseDto(E entity) {
        this.id = entity.getId();
    }

    public abstract E toEntity();
}
