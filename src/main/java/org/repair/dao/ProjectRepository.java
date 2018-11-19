package org.repair.dao;

import org.repair.model.Project;
import org.repair.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByExecutor(Worker executor);
}
