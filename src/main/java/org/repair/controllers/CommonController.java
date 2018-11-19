package org.repair.controllers;

import org.repair.dao.ProjectRepository;
import org.repair.dao.WorkerRepository;
import org.repair.model.Project;
import org.repair.model.Worker;
import org.repair.report.services.generator.ReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
public class CommonController {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReportGenerator reportGenerator;

    @GetMapping(value = "/principal")
    public String getPrincipal(@AuthenticationPrincipal UserDetails principal) {
        return principal.getUsername();
    }

    @GetMapping(value = "/workers")
    public List<Worker> getWorkerInfo() {
        Iterable<Worker> workers = workerRepository.findAll();
        return (ArrayList<Worker>) workers;
    }

    @GetMapping(value = "/projects")
    public List<Project> getProjects(@AuthenticationPrincipal UserDetails principal) {
        Worker worker = workerRepository.findOneByName(principal.getUsername());
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

    @PostMapping(value = "/project/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Project createProject(@AuthenticationPrincipal final UserDetails principal,
                                 MultiValueMap<String, String> inputData) {
        Worker worker = workerRepository.findOneByName(principal.getUsername());
        Project project = new Project(inputData.getFirst("clientname"), inputData.getFirst("clientphone"),
                inputData.getFirst("street"), inputData.getFirst("streetumber"));
        project.setExecutor(worker);
        Project saved = projectRepository.save(project);
        return saved;
    }
}
