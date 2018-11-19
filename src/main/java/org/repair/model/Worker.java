package org.repair.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
public class Worker implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;
    @JsonIgnore
    @Column
    private String password;
    private String role;
    @OneToMany(targetEntity = Project.class, mappedBy = "executor")
    @JsonIgnore
    private Set<Project> projects;

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

    public Long getId() {
        return id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Project> getProjects() {
        return Collections.unmodifiableSet(projects);
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
