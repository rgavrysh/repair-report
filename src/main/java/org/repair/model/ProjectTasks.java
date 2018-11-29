package org.repair.model;

import java.io.Serializable;

//@Entity
//@Table(name = "PROJECT_TASKS")
//@IdClass(ProjectTasksId.class)
public class ProjectTasks implements Serializable {
    private static final long serialVersionUID = 1l;

//    @Id
//    @Column(name = "PRJ_ID")
    private Long prjId;
//    @Id
//    @Column(name = "TASK_ID")
    private Long taskId;
    private Double qty;

    public ProjectTasks() {
    }

    public ProjectTasks(Long prjId, Long taskId) {
        this.prjId = prjId;
        this.taskId = taskId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
