package org.repair.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.repair.PrintReport;
import org.repair.model.Address;
import org.repair.model.ObjectDimensions;
import org.repair.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
public class RestControllerTests {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("vova")
    public void whenGetWorkersThenAllFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/workers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    @WithUserDetails("andriy")
    public void givenWorkerWhenGetProjectsThenAllFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @WithUserDetails("vova")
    public void givenWorkerWhenGetProjectThenRightFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/project/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("clientName").value("roman"));
    }

    @Test
    @WithUserDetails("vova")
    public void givenWorkerWhenUpdateProjectThenSaved() throws Exception {
        Address address = new Address("Lviv", "Bandery", "1", "1", "79000");
        ObjectDimensions dimensions = new ObjectDimensions(0.0, 0.0, 0.0, 0.0);
        Project project = new Project("Roman", "380979617254", address, dimensions);
        mockMvc.perform(MockMvcRequestBuilders.put("/project/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(project)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("address.street").value("Bandery"));
    }

    @Test
    @WithUserDetails("vova")
    public void givenWorkerWhenGetTasksDescriptionsThenAllReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/descriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[\"walls painting\",\"bricks construction\"]"));
    }

    @Test
    @WithUserDetails("vova")
    public void givenWorkerWhenDownloadReportThenFileGenerated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/download/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();
        response.containsHeader("Content-Disposition");
        Assert.assertTrue(response.getContentLength() > 0);
    }


}
