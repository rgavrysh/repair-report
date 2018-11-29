package org.repair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Worker implements Serializable {

    private static final long serialVersionUID = 1l;

    private String id;
    private String name;
    @JsonIgnore
    private String password;
    private String role;
    @JsonIgnore
    @DBRef(db = "report")
    private List<Project> projects;

    public Worker() {
    }

    public Worker(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Project> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return Objects.equals(id, worker.id) &&
                Objects.equals(name, worker.name) &&
                Objects.equals(role, worker.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, role);
    }
}
