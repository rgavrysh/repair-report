package org.repair.model;

import java.io.Serializable;
import java.util.Objects;
//todo: use this class to decompose tariff from task
public class WorkerTariff implements Serializable {
    private static final long serialVersionUID = 1l;

    private Long workerId;
    private Long taskId;
    private Double tariff;

    public WorkerTariff() {
    }

    public WorkerTariff(Long workerId, Long taskId) {
        this.workerId = workerId;
        this.taskId = taskId;
    }

    public Double getTariff() {
        return tariff;
    }

    public void setTariff(Double tariff) {
        this.tariff = tariff;
    }

    @Override
    public String toString() {
        return "{" +
                "workerId=" + workerId +
                ", taskId=" + taskId +
                ", tariff=" + tariff +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerTariff that = (WorkerTariff) o;
        return Objects.equals(workerId, that.workerId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerId, taskId);
    }
}
