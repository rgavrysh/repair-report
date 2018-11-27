package org.repair.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.collections4.map.HashedMap;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //todo: use UUID (it is exposed within URL)
    private Long id;
    @Column(name = "CLIENT_NAME")
    private String clientName;
    @Column(name = "CLIENT_PHONE")
    private String clientPhone;
    @OneToOne(targetEntity = Address.class, cascade = CascadeType.PERSIST)
    private Address address;
    //todo: use @Embedded object for dimensions
    private Double walls;
    private Double floor;
    private Double ceiling;
    private Double slopes;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "WORKER_ID")
    private Worker executor;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "PROJECT_TASKS", joinColumns = @JoinColumn(name = "PRJ_ID"))
    @MapKeyJoinColumn(name = "TASK_ID")
    @Column(name = "QTY")
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

    public Long getId() {
        return id;
    }

    public Worker getExecutor() {
        return executor;
    }

    public void setExecutor(Worker executor) {
        this.executor = executor;
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