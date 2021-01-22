package com.testtask.nauka.testUtils;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.*;
import java.util.stream.Stream;

@Service
public class TruncateDbService {

    private final EntityManager em;

    public TruncateDbService(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Transactional
    public void truncate() {
        try {
            Stream<String> entityNames = em.getMetamodel().getManagedTypes().stream()
                    .filter(i -> !i.getJavaType().isAnnotation())
                    .map(i -> i.getJavaType().getName());
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            entityNames
                    .map(name -> em.createQuery("DELETE FROM " + name))
                    .forEach(i -> {
                        i.executeUpdate();
                        em.flush();
                    });
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        } catch (SecurityException | IllegalStateException e) {
            e.printStackTrace();
        }
    }


}
