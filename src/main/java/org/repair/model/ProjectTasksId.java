package org.repair.model;

import java.io.Serializable;
import java.util.Objects;

public class ProjectTasksId implements Serializable {
    private static final long serialVersionUID = 1l;

    private Long prjId;
    private Long taskId;

    public ProjectTasksId() {
    }

    public ProjectTasksId(Long prjId, Long taskId) {
        this.prjId = prjId;
        this.taskId = taskId;
    }

    public Long getPrjId() {
        return prjId;
    }

    public Long getTaskId() {
        return taskId;
    }

    @Override
    public String toString() {
        return "{" +
                "prjId=" + prjId +
                ", taskId=" + taskId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTasksId that = (ProjectTasksId) o;
        return Objects.equals(prjId, that.prjId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prjId, taskId);
    }
}
