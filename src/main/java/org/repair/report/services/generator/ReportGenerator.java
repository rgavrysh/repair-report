package org.repair.report.services.generator;

import org.repair.model.Project;

import java.io.FileNotFoundException;

public interface ReportGenerator {
    String generateReport(Project project, String fileName) throws FileNotFoundException;
}
