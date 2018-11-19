package org.repair.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TASK")
public class JobTask implements Serializable {

    private static final long serialVersionUID = 1l;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SHORT_DESC")
    private String shortDescription;
    @Column(name = "DESCR")
    private String description;
    private Double tariff;

    public JobTask() {
    }

    public JobTask(String dsc, Double tariff) {
        this.shortDescription = dsc;
        this.tariff = tariff;
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

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", shortDescription:'" + shortDescription + '\'' +
                ", description:'" + description + '\'' +
                ", tariff:" + tariff +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobTask jobTask = (JobTask) o;
        return Objects.equals(id, jobTask.id) &&
                Objects.equals(shortDescription, jobTask.shortDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortDescription);
    }
}
