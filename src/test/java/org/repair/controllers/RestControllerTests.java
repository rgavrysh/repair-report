package org.repair.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.repair.PrintReport;
import org.repair.dao.ProjectRepository;
import org.repair.dao.TaskRepository;
import org.repair.dao.WorkerRepository;
import org.repair.model.*;
import org.repair.services.LoginDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrintReport.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RestControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    @MockBean
    private WorkerRepository workerRepository;
    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private TaskRepository taskRepository;

    private Worker worker;
    private Project testProject;
    private JobTask task;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        testProject = createTestProject();
        task = new JobTask("Test desc", 10.0, 100.0);
        task.setDescription(task.getShortDescription());
        Worker mockedWorker = new Worker("vova", "$2a$11$N6PHp0OR0dtbRsPPU6.Hc.5s3vV2ATV60KkqhOIMuIjjUPdCwWobK");
        mockedWorker.setRole("ADMIN");
        Mockito.when(workerRepository.findOneByName("vova")).thenReturn(mockedWorker);
        Mockito.when(workerRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(mockedWorker)));
        Mockito.when(projectRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(testProject));
        Mockito.when(projectRepository.save(testProject)).thenReturn(testProject);
        Mockito.when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(taskRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(task));
        Mockito.when(taskRepository.findOneByShortDescriptionAndTariff("Task desc", 10.0)).thenReturn(java.util.Optional.ofNullable(task));
        Mockito.when(taskRepository.save(task)).thenReturn(task);

    }

    @Test
    public void whenGetWorkersThenAllFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/workers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void givenWorkerWhenGetProjectThenRightFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/project/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("clientName").value("Roman"));
    }

    @Test
    public void givenWorkerWhenUpdateProjectThenSaved() throws Exception {
        testProject.getAddress().setStreet("Bandery");
        mockMvc.perform(MockMvcRequestBuilders.put("/project/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(testProject)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("address.street").value("Bandery"));
    }

    @Test
    public void givenWorkerWhenGetTasksDescriptionsThenAllReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/descriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void givenWorkerWhenDownloadReportThenFileGenerated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/download/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        Assert.assertTrue(response.containsHeader("Content-Disposition"));
        Assert.assertTrue(response.getContentLength() > 0);
    }

    @Test
    public void givenWorkerWhenAddTaskThenNewCreated() throws Exception {
        task = jsonMapper.readValue(mockMvc.perform(
                MockMvcRequestBuilders.post("/project/1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(task))
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("Test desc"))
                .andReturn().getResponse().getContentAsString(), JobTask.class);
    }

    @Test
    public void givenWorkerWhenDeleteTaskThenRemoved() throws Exception {
        testProject.addCustomJobTask(task);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/project/1/task/1")
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Autowired
    private LoginDetailService loginDetailService;

    @Test
    public void givenLoginDetailsWhenCheckPriviledgesThenCorrectReturned() throws Exception {
        Worker worker = new Worker("vova", "$2a$11$N6PHp0OR0dtbRsPPU6.Hc.5s3vV2ATV60KkqhOIMuIjjUPdCwWobK");
        worker.setRole("ADMIN");
        LoginDetailService.WorkerDetail workerDetail = new LoginDetailService.WorkerDetail(worker);
        Mockito.when(workerRepository.findOneByName("vova")).thenReturn(workerDetail);
        UserDetails user = loginDetailService.loadUserByUsername("vova");
        Assert.assertNotNull(user);
        Assert.assertTrue(user.isAccountNonExpired());
        Assert.assertTrue(user.isEnabled());
        Assert.assertTrue(user.isCredentialsNonExpired());
        Assert.assertTrue(user.isAccountNonLocked());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void givenInvalidLoginDetailsWhenGetUserDetailsThenThrowsException() throws Exception {
        Mockito.when(workerRepository.findOneByName("vova")).thenReturn(null);
        loginDetailService.loadUserByUsername("vova");
    }

    private Project createTestProject() {
        Address address = new Address("Lviv", "Naukova", "1", "1", "79000");
        ObjectDimensions dimensions = new ObjectDimensions(0.0, 0.0, 0.0, 0.0);
        return new Project("Roman", "380979617254", address, dimensions);
    }
}
