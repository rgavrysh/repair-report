package org.repair.report.services.generator;

import org.repair.model.Project;
import org.wickedsource.docxstamper.DocxStamper;
import org.wickedsource.docxstamper.DocxStamperConfiguration;

import java.io.*;

public class DocxGenerator implements ReportGenerator {
    @Override
    public String generateReport(Project project, String fileName) throws FileNotFoundException {
        DocxStamper<Object> stamper = new DocxStamper<>(new DocxStamperConfiguration());
        File file = prepareFileForReport(fileName);
        try (FileInputStream template = new FileInputStream(new File("reportTemplate.docx"))) {
            FileOutputStream report = new FileOutputStream(file);

            stamper.stamp(template, project, report);
            report.close();
        } catch (IOException e) {
            LOG.error("Failed to generate report.");
            return null;
        }
        return file.getAbsolutePath();
    }
}