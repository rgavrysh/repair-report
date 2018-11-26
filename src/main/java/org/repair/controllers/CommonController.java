package org.repair.controllers;

import org.repair.dao.ProjectRepository;
import org.repair.dao.ProjectTasksRepository;
import org.repair.dao.TaskRepository;
import org.repair.dao.WorkerRepository;
import org.repair.dto.ProjectDTO;
import org.repair.dto.TaskDTO;
import org.repair.model.*;
import org.repair.report.services.generator.ReportGenerator;
import org.repair.services.LoginDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController(value = "/api")
public class CommonController {
    private static final Logger LOG = LoggerFactory.getLogger("CONTROLLER");
    private static final String REPORTS_FOLDER_PREFIX = "./reports/";

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

    @GetMapping(value = "/principal")
    public String getPrincipal(@AuthenticationPrincipal LoginDetailService.WorkerDetail principal) {
        if (principal != null) {
            return principal.getUsername();
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
        Worker worker = workerRepository.findOneByName(principal.getUsername());
        return projectRepository.findByExecutor(worker);
    }

    @GetMapping(value = "/project/{id}")
    public Project getProjectById(@PathVariable("id") int id) {
        Optional<Project> project = projectRepository.findById(Long.valueOf(id));
        return project.orElseThrow(EntityNotFoundException::new);
    }

    @PutMapping(value = "/project/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Project updateProject(@PathVariable("id") final int id, @RequestBody final ProjectDTO projectDTO) {
        Optional<Project> project = projectRepository.findById(Long.valueOf(id));
        Project updated = project.orElseThrow(EntityNotFoundException::new).update(projectDTO);
        return projectRepository.save(updated);
    }

    @GetMapping(value = "/download/{id}")
    public FileSystemResource downloadFile(@PathVariable("id") int id, HttpServletResponse response) throws IOException {
        Optional<Project> project = projectRepository.findById(Long.valueOf(id));
        String fileName;
        try {
            fileName = reportGenerator.generateReport(project.orElseThrow(EntityNotFoundException::new),
                    REPORTS_FOLDER_PREFIX + String.valueOf(id) + "/" + Instant.now().toString());
        } catch (FileNotFoundException e) {
            LOG.error("File could not be found.");
            throw new FileNotFoundException();
        }
        File file = new File(fileName);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\".docx", file.getName()));
        return new FileSystemResource(file);
    }

    @PostMapping(value = "/project", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody ProjectDTO projectDTO, @AuthenticationPrincipal LoginDetailService.WorkerDetail principal) {
        Project project = new Project(projectDTO.getClientName(), projectDTO.getClientPhone(), projectDTO.getStreet(), projectDTO.getStreetNumber());
        Optional<Worker> executor = workerRepository.findById(principal.getId());
        project.setExecutor(executor.orElseThrow(EntityNotFoundException::new));
        return projectRepository.save(project);
    }

    @GetMapping(value = "/tasks/descriptions")
    public Set<String> getTasksDescriptions() {
        List<JobTask> tasks = taskRepository.findAll();
        return tasks.stream().map(jt -> jt.getShortDescription()).collect(Collectors.toSet());
    }

    @PostMapping(value = "/task")
    @ResponseStatus(HttpStatus.CREATED)
    public JobTask addNewTaskToProject(@PathParam("project") final int id, @RequestBody TaskDTO taskDTO) {
        JobTask jobTask = new JobTask(taskDTO.getShortDescription(), taskDTO.getTariff());
        //duplicate short description to description
        jobTask.setDescription(taskDTO.getShortDescription());
        JobTask saved = taskRepository.save(jobTask);
        ProjectTasks projectTasks = new ProjectTasks(Long.valueOf(id), saved.getId());
        projectTasks.setQty(taskDTO.getQty());
        projectTasksRepository.save(projectTasks);
        return saved;
    }

    @DeleteMapping(value = "/project/{projectId}/task/{taskId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTask(@PathVariable("projectId") final int projectId, @PathVariable("taskId") final int taskId) {
        ProjectTasksId projectTasksId = new ProjectTasksId(Long.valueOf(projectId), Long.valueOf(taskId));
        projectTasksRepository.deleteById(projectTasksId);
    }
}
