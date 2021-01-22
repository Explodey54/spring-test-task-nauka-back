package com.testtask.nauka.api.workers.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findAllByDepartmentId(Long id);
}
