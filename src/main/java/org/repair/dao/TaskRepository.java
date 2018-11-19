package org.repair.dao;

import org.repair.model.JobTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<JobTask, Long> {
}
