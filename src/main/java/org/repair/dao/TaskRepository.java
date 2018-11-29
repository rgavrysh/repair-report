package org.repair.dao;

import org.repair.model.JobTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<JobTask, Long> {
    Optional<JobTask> findOneByShortDescriptionAndTariff(String shortDescription, Double tariff);
}
