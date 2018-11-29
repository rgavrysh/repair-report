package org.repair.controllers;

import org.repair.dao.ProjectRepository;
import org.repair.dao.TaskRepository;
import org.repair.dao.WorkerRepository;
import org.repair.model.JobTask;
import org.repair.model.Project;
import org.repair.model.Worker;
import org.repair.report.services.generator.ReportGenerator;
import org.repair.services.LoginDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController(value = "/api")
public class CommonController {
    private static final Logger LOG = LoggerFactory.getLogger("CONTROLLER");
    private static final String REPORTS_FOLDER_PREFIX = "./reports";

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

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
    public List<Project> getProjects(@AuthenticationPrincipal LoginDetailService.WorkerDetail principal) {
        return principal.getWorker().getProjects();
    }

    @GetMapping(value = "/project/{id}")
    public Project getProjectById(@PathVariable("id") String id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElseThrow(RuntimeException::new);
    }

    @PutMapping(value = "/project/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Project updateProject(@PathVariable("id") final String id, @RequestBody final Project modified) {
        Optional<Project> project = projectRepository.findById(id);
        Project updated = project.orElseThrow(RuntimeException::new).update(modified);
        return projectRepository.save(updated);
    }

    @GetMapping(value = "/download/{id}")
    public FileSystemResource downloadFile(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        Optional<Project> project = projectRepository.findById(id);
        String fileName;
        try {
            fileName = reportGenerator.generateReport(project.orElseThrow(RuntimeException::new),
                    Paths.get(REPORTS_FOLDER_PREFIX, String.valueOf(id), Instant.now().toString()).toString());
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
    public Project createProject(@RequestBody Project project, @AuthenticationPrincipal LoginDetailService.WorkerDetail principal) {
        project.setWorkerId(principal.getId());
        principal.getWorker().addProject(project);
        Project savedProject = projectRepository.save(project);
        workerRepository.save(principal.getWorker());
        return savedProject;
    }

    @GetMapping(value = "/tasks/descriptions")
    public Set<String> getTasksDescriptions() {
        List<JobTask> tasks = taskRepository.findAll();
        return tasks.stream().map(JobTask::getShortDescription).collect(Collectors.toSet());
    }

    @PostMapping(value = "/project/{projectId}/task")
    @ResponseStatus(HttpStatus.CREATED)
    public JobTask addNewTaskToProject(@PathVariable("projectId") final String projectId, @RequestBody JobTask newTask) {
        Project project = projectRepository.findById(projectId).get();
        Optional<JobTask> repositoryOneByShortDescriptionAndTariff =
                taskRepository.findOneByShortDescriptionAndTariff(newTask.getShortDescription(), newTask.getTariff());
        if (repositoryOneByShortDescriptionAndTariff.isPresent()) {
            JobTask jobTask = repositoryOneByShortDescriptionAndTariff.get();
            jobTask.setProjectId(projectId);
            JobTask savedTask = taskRepository.save(jobTask);
            project.addCustomJobTask(savedTask);
            projectRepository.save(project);
            return savedTask;
        }
        //duplicate short-description to description
        newTask.setDescription(newTask.getShortDescription());
        newTask.setProjectId(projectId);
        JobTask savedTask = taskRepository.save(newTask);
        project.addCustomJobTask(savedTask);
        projectRepository.save(project);
        return savedTask;
    }

    @DeleteMapping(value = "/project/{projectId}/task/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("projectId") final String projectId, @PathVariable("taskId") final String taskId) {
        JobTask task = taskRepository.findById(taskId).get();
        Project project = projectRepository.findById(projectId).get();
        project.removeTask(task);
        projectRepository.save(project);
        taskRepository.deleteById(taskId);
    }
}
