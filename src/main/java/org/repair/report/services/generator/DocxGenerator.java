/*
package org.repair.report.services.generator;

import org.repair.model.Project;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

import java.io.*;

public class DocxGenerator implements ReportGenerator {
    @Override
    public void generateReport(Project project, String fileName) throws FileNotFoundException {
        DocxStamper<Object> stamper = new DocxStamper<>(new DocxStamperConfiguration());
        try (FileInputStream template = new FileInputStream(new File("reportTemplate.docx"))) {
            FileOutputStream report = new FileOutputStream(new File(fileName));

            stamper.stamp(template, project, report);
            report.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate report");
        }
    }
}
*/