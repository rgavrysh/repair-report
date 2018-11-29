package org.repair.dao;

import org.repair.model.ProjectTasks;
import org.repair.model.ProjectTasksId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTasksRepository extends MongoRepository<ProjectTasks, ProjectTasksId> {
}
