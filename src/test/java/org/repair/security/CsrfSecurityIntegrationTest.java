package org.repair.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.repair.PrintReport;
import org.repair.dto.ProjectDTO;
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

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void contextInitializationTest() {
        Assert.assertNotNull(context);
    }

    @Test
    @WithUserDetails("andriy")
    public void givenCsrfWhenAddProjectThenCreated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createProject())
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithUserDetails("vova")
    public void givenUserWhenGetPrincipalThenReturnRightName() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/principal")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("vova"));
    }

    private String createProject() throws JsonProcessingException {
        ProjectDTO projectDTO = new ProjectDTO("Test", "0000", "StreetTest", "0");
        return new ObjectMapper().writeValueAsString(projectDTO);
    }
}
