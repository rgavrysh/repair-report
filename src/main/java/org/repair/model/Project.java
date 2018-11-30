package org.repair.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String clientName;
    private String clientPhone;
    private Address address;
    private ObjectDimensions dimensions;
    @JsonBackReference
    private String workerId;
    @DBRef(db = "report")
    private Set<JobTask> tasks;

    public Project() {
    }

    public Project(String clientName, String clientPhone, Address address, ObjectDimensions dimensions) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.address = address;
        this.dimensions = dimensions;
    }

    public String getId() {
        return id;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ObjectDimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(ObjectDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Set<JobTask> getTasks() {
        return (tasks != null) ? Collections.unmodifiableSet(tasks) : Collections.emptySet();
    }

    @Override
    public String toString() {
        return "{" +
                "clientName='" + clientName + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", address=" + address +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) &&
                Objects.equals(address, project.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }

    public boolean addCustomJobTask(JobTask jobTask) {
        if (this.tasks == null) {
            this.tasks = new HashSet<>();
        }
        return this.tasks.add(jobTask);
    }

    public boolean removeTask(JobTask jobTask) {
        if (this.tasks == null || this.tasks.isEmpty()) {
            return false;
        }
        return this.tasks.remove(jobTask);
    }

    public Project update(Project updated) {
        setClientName(updated.getClientName());
        setClientPhone(updated.getClientPhone());
        setAddress(updated.getAddress());
        setDimensions(updated.getDimensions());
        return this;
    }
}