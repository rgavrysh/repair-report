package org.repair.dao;

import org.repair.model.ProjectTasks;
import org.repair.model.ProjectTasksId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTasksRepository extends JpaRepository<ProjectTasks, ProjectTasksId>{
}
