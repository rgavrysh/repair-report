package org.repair.report;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.repair.model.Address;
import org.repair.model.JobTask;
import org.repair.model.Project;
import org.repair.report.services.generator.PoiGenerator;
import org.repair.report.services.generator.ReportGenerator;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

public class PrintReportTest {
    private Project project = null;
    private String reportName;

    @Before
    public void setup() {
        reportName = "./src/test/resources/wordPoiReport.docx";
        // TODO: 27/11/2018 Give JSON as input
        Address address = new Address("Lviv", "Bandery", "1", "1", "79000");
        project = new Project("Roman", "380979617254", address, 0.0, 0.0, 0.0, 0.0);
        project.addCustomJobTask(new JobTask("Wall Painting", 15.0), 100.0);
        project.addCustomJobTask(new JobTask("Bricks construction", 35.0), 60.0);
    }

    @Test
    public void givenProject_whenPrintReport_thenReportDocumentGenerated() {
        ReportGenerator reportGenerator = new PoiGenerator();
        try {
            reportGenerator.generateReport(project, reportName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File Not Found");
        }
        File generatedReport = new File(reportName);
        assertTrue(generatedReport.exists());
    }

    @After
    public void tearDown() {
        File file = new File(reportName);
        file.deleteOnExit();
    }
}