package org.repair.controllers;

import org.repair.dao.ProjectRepository;
import org.repair.dao.ProjectTasksRepository;
import org.repair.dao.TaskRepository;
import org.repair.dao.WorkerRepository;
import org.repair.dto.ProjectDTO;
import org.repair.dto.TaskDTO;
import org.repair.model.JobTask;
import org.repair.model.Project;
import org.repair.model.ProjectTasks;
import org.repair.model.Worker;
import org.repair.report.services.generator.ReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CommonController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTasksRepository projectTasksRepository;

    @Autowired
    private ReportGenerator reportGenerator;
    private Worker worker;

    @GetMapping(value = "/principal")
    public String getPrincipal(@AuthenticationPrincipal Worker principal) {
        if (principal != null) {
            this.worker = principal;
            return principal.getName();
        }
        return "anonymous";
    }

    @GetMapping(value = "/workers")
    public List<Worker> getWorkerInfo() {
        Iterable<Worker> workers = workerRepository.findAll();
        return (ArrayList<Worker>) workers;
    }

    @GetMapping(value = "/projects")
    public List<Project> getProjects(@AuthenticationPrincipal UserDetails principal) {
        worker = workerRepository.findOneByName(principal.getUsername());
        List<Project> byWorkerId = projectRepository.findByExecutor(worker);
        return byWorkerId;
    }

    @GetMapping(value = "/project/{id}")
    public Project getProjectById(@PathVariable("id") int id) {
        //todo: check authorization
        Optional<Project> project = projectRepository.findById(Long.valueOf(id));
        return project.get();
    }

    @GetMapping(value = "/download/{id}")
    public FileSystemResource downloadFile(@PathVariable("id") int id, HttpServletResponse response) throws IOException {
        Optional<Project> project = projectRepository.findById(Long.valueOf(id));
        String fileName = null;
        try {
            //todo: check for file existence before generating new report
            fileName = reportGenerator.generateReport(project.get(), String.valueOf(id));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File file = new File(fileName);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\".docx", file.getName()));
        return new FileSystemResource(file);
    }

    @PostMapping(value = "/project", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Project createProject(@RequestBody ProjectDTO projectDTO) {
        Project project = new Project(projectDTO.getClientName(), projectDTO.getClientPhone(), projectDTO.getStreet(), projectDTO.getStreetNumber());
        Worker executor = workerRepository.findById(this.worker.getId()).get();
        project.setExecutor(executor);
        Project saved = projectRepository.save(project);
        return saved;
    }

    @GetMapping(value = "/tasks/descriptions")
    public List<String> getTasksDescriptions() {
        List<JobTask> tasks = taskRepository.findAll();
        List<String> descriptions = tasks.stream().map(jt -> jt.getShortDescription()).collect(Collectors.toList());
        return descriptions;
    }

    @PostMapping(value = "/task")
    public JobTask addNewTaskToProject(@PathParam("project") final int id, @RequestBody TaskDTO taskDTO) {
        JobTask jobTask = new JobTask(taskDTO.getShortDescription(), taskDTO.getTariff());
        //todo: duplicate short description if description is null
        jobTask.setDescription(taskDTO.getShortDescription());
        JobTask saved = taskRepository.save(jobTask);
        ProjectTasks projectTasks = new ProjectTasks(Long.valueOf(id), saved.getId());
        projectTasks.setQty(taskDTO.getQty());
        projectTasksRepository.save(projectTasks);
        return saved;
    }
}
