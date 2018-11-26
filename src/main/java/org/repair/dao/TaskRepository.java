package org.repair.dao;

import org.repair.model.JobTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<JobTask, Long> {
    Optional<JobTask> findOneByShortDescriptionAndTariff(String shortDescription, Double tariff);
}
