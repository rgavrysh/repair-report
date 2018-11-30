package org.repair.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.repair.PrintReport;
import org.repair.controllers.CommonController;
import org.repair.model.Address;
import org.repair.model.ObjectDimensions;
import org.repair.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = PrintReport.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CsrfSecurityIntegrationTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private CommonController controller;
    @Autowired
    private ObjectMapper jsonMapper;

    private Project testProject;

    @Before
    public void setup() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        testProject = createProject();
    }

    @Test
    public void contextInitializationTest() {
        Assert.assertNotNull(context);
    }

    @Test
    @WithUserDetails("vova")
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
    @WithUserDetails("vova")
    public void givenUserWithNoCSRF_WhenAddProject_ThenForbidden() throws Exception {
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
    @WithUserDetails("vova")
    public void givenUserWhenGetPrincipalThenReturnRightName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/principal")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("vova"));
    }

    @After
    @WithUserDetails("vova")
    public void tearDown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/project/" + testProject.getId())
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        testProject = null;
    }

    private Project createProject() throws JsonProcessingException {
        Address address = new Address("Lviv", "Bandery", "1", "1", "79000");
        ObjectDimensions dimensions = new ObjectDimensions(0.0, 0.0, 0.0, 0.0);
        return new Project("Roman", "380979617254", address, dimensions);
    }
}
