package com.testtask.nauka.common;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

abstract public class AbstractCrudService<T, ID>  {
    private CrudRepository<T, ID> getMainRepository() {
        throw new Error("Main repository is not set");
    }

    public Optional<T> findById(ID id) {
        return getMainRepository().findById(id);
    }

    public T save(T object) {
        return getMainRepository().save(object);
    }

    public void deleteById(ID id) {
        getMainRepository().deleteById(id);
    }
}

