package org.repair.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.repair.PrintReport;
import org.repair.controllers.CommonController;
import org.repair.dao.ProjectRepository;
import org.repair.dao.WorkerRepository;
import org.repair.model.*;
import org.repair.services.LoginDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PrintReport.class, MockLoginDetailService.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CsrfSecurityIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private WorkerRepository workerRepository;
    @MockBean
    private ProjectRepository projectRepository;
    @Autowired
    private CommonController controller;
    @Autowired
    private ObjectMapper jsonMapper;


    private Project testProject;
    private JobTask task;
    private Worker worker;
    private MockMvc mockMvc;

    @Before
    public void setup() throws JsonProcessingException {
        testProject = createProject();
        task = new JobTask("Test desc", 10.0, 100.0);
        task.setDescription(task.getShortDescription());
        worker = new Worker("vova", "$2a$11$N6PHp0OR0dtbRsPPU6.Hc.5s3vV2ATV60KkqhOIMuIjjUPdCwWobK");
        worker.setRole("ADMIN");
        Mockito.when(workerRepository.findOneByName("vova")).thenReturn(worker);
        Mockito.when(workerRepository.save(worker)).thenReturn(worker);
        Mockito.when(projectRepository.save(testProject)).thenReturn(testProject);
        Mockito.when(projectRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(testProject));

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = "vova", userDetailsServiceBeanName = "mockLoginDetailService")
    public void givenCsrfWhenAddProjectThenCreated() throws Exception {
        testProject = jsonMapper.readValue(mockMvc.perform(
                MockMvcRequestBuilders.post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(testProject))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString(), Project.class);
    }

    @Test
    @WithUserDetails(value = "vova", userDetailsServiceBeanName = "mockLoginDetailService")
    public void givenUserWithNoCSRFWhenAddProjectThenForbidden() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(testProject))
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenNullWhenGetPrincipalThenReturnDefault() throws Exception {
        Assert.assertEquals("anonymous", controller.getPrincipal(null));
    }

    @Test
    @WithUserDetails(value = "vova", userDetailsServiceBeanName = "mockLoginDetailService")
    public void givenUserWhenGetPrincipalThenReturnRightName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/principal")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("vova"));
    }

    @Test
    @WithUserDetails(value = "vova", userDetailsServiceBeanName = "mockLoginDetailService")
    public void whenGetWorkersThenAllFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @WithUserDetails(value = "vova", userDetailsServiceBeanName = "mockLoginDetailService")
    public void givenWorkerWhenDeleteProjectThenResponseNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/project/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Autowired
    CommonController commonController;
    @Autowired
    MockLoginDetailService mockLoginDetailService;

    @Test
    public void givenWorkerWhenDeleteProjectThenWorkerNotContains() {
        worker.addProject(testProject);
        LoginDetailService.WorkerDetail workerDetail = new LoginDetailService.WorkerDetail(worker);
        commonController.deleteProject("1", workerDetail);
        Assert.assertTrue(worker.getProjects().isEmpty());
    }

    private Project createProject() throws JsonProcessingException {
        Address address = new Address("Lviv", "Bandery", "1", "1", "79000");
        ObjectDimensions dimensions = new ObjectDimensions(0.0, 0.0, 0.0, 0.0);
        return new Project("Roman", "380979617254", address, dimensions);
    }
}
