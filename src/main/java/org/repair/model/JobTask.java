package org.repair.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.util.Objects;

public class JobTask implements Serializable {

    private static final long serialVersionUID = 1l;
    private String id;
    private String shortDescription;
    private String description;
    private Double tariff;
    private Double quantity;
    @JsonBackReference
    private String projectId;

    public JobTask() {
    }

    public JobTask(String dsc, Double tariff) {
        this.shortDescription = dsc;
        this.tariff = tariff;
    }

    public JobTask(String dsc, Double tariff, Double qty) {
        this.shortDescription = dsc;
        this.tariff = tariff;
        this.quantity = qty;
    }

    public String getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTariff() {
        return tariff;
    }

    public void setTariff(Double tariff) {
        this.tariff = tariff;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", shortDescription:'" + shortDescription + '\'' +
                ", description:'" + description + '\'' +
                ", quantity:'" + quantity + '\'' +
                ", tariff:" + tariff +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobTask jobTask = (JobTask) o;
        return Objects.equals(id, jobTask.id) &&
                Objects.equals(shortDescription, jobTask.shortDescription) &&
                Objects.equals(tariff, jobTask.tariff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortDescription, tariff);
    }
}
