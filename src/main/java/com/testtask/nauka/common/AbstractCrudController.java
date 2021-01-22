package com.testtask.nauka.common;

import com.testtask.nauka.common.response.SuccessResponse;
import lombok.Getter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public abstract class AbstractCrudController<T extends BaseEntity<T>, D extends BaseDto<D, T>> {
    @Autowired
    private EntityManager entityManager;
    @Getter
    private final JpaRepository<T, Long> repository;
    private final Class<T> entityClass;
    private final Class<D> dtoClass;

    public AbstractCrudController(JpaRepository<T, Long> repository, Class<T> entityClass, Class<D> dtoClass) {
        this.repository = repository;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public SuccessResponse<List<D>> getAll() {
        List<D> output = getRepository().findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return new SuccessResponse<>(output);
    }

    public SuccessResponse<D> getById(Long id) {
        Optional<T> found = getRepository().findById(id);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, getClassName() + " not found");
        }
        return new SuccessResponse<>(this.entityToDto(found.get()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<D> create(D input) {
        T inputToEntity = input.toEntity();
        T saved = getRepository().saveAndFlush(inputToEntity);
        entityManager.refresh(saved);
        return new SuccessResponse<>(this.entityToDto(saved));
    }

    public SuccessResponse<D> update(D input, Long id) {
        Optional<T> found = getRepository().findById(id);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, getClassName() + " not found");
        }
        T entity = found.get();
        entity.merge(input.toEntity());
        T saved = getRepository().saveAndFlush(entity);
        entityManager.refresh(saved);
        System.out.println(saved);
        return new SuccessResponse<>(this.entityToDto(saved));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        Optional<T> found = getRepository().findById(id);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, getClassName() + " not found");
        }
        getRepository().deleteById(id);
    }

    protected String getClassName() {
        return entityClass.getSimpleName();
    }

    protected D entityToDto(T entity) throws ResponseStatusException {
        try {
            return dtoClass.getConstructor(this.entityClass).newInstance(entity);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getTargetException().getMessage());
        }
    }
}

