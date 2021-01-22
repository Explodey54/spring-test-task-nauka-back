package com.testtask.nauka.testUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntitySaverService implements InitializingBean {

    @Autowired
    private WebApplicationContext appContext;
    private Repositories repositories;

    @Override
    public void afterPropertiesSet() {
        repositories = new Repositories(appContext);
    }

    public <T> T save(T entity) {
        Optional<Object> found = repositories.getRepositoryFor(entity.getClass());
        if (found.isPresent()) {
            CrudRepository<T, Object> repository = (CrudRepository<T, Object>) found.get();
            return repository.save(entity);
        } else {
            throw new RuntimeException("Repository for class '" + entity.getClass().getName() + "' not found");
        }
    }

    public <T> List<T> saveMultiple(List<T> entityList) {
        return entityList.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

}
