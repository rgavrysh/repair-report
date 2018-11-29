package org.repair.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String clientName;
    private String clientPhone;
    private Address address;
    //todo: use @Embedded object for dimensions
    private Double walls;
    private Double floor;
    private Double ceiling;
    private Double slopes;

    @JsonBackReference
    private String workerId;

    @JsonSerialize(keyUsing = TasksSerializer.class)
    @JsonDeserialize(keyUsing = TasksDeserializer.class)
    private Map<JobTask, Double> tasks = new HashedMap<>();

    public Project() {
    }

    public Project(String clientName, String clientPhone, Address address, Double walls, Double floor, Double ceiling, Double slopes) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.address = address;
        this.walls = walls;
        this.floor = floor;
        this.ceiling = ceiling;
        this.slopes = slopes;
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

    public Double getWalls() {
        return walls;
    }

    public void setWalls(Double walls) {
        this.walls = walls;
    }

    public Double getFloor() {
        return floor;
    }

    public void setFloor(Double floor) {
        this.floor = floor;
    }

    public Double getCeiling() {
        return ceiling;
    }

    public void setCeiling(Double ceiling) {
        this.ceiling = ceiling;
    }

    public Double getSlopes() {
        return slopes;
    }

    public void setSlopes(Double slopes) {
        this.slopes = slopes;
    }

    public Map<JobTask, Double> getTasks() {
        return Collections.unmodifiableMap(this.tasks);
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

    public boolean addCustomJobTask(JobTask jobTask, Double quantity) {
        this.tasks.put(jobTask, quantity);
        return true;
    }

    public Project update(Project updated) {
        setClientName(updated.getClientName());
        setClientPhone(updated.getClientPhone());
        setAddress(updated.getAddress());
        setWalls(updated.getWalls());
        setFloor(updated.getFloor());
        setCeiling(updated.getCeiling());
        setSlopes(updated.getSlopes());
        return this;
    }
}