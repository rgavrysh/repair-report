package org.repair.report.services.generator;

import org.repair.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface ReportGenerator {
    Logger LOG = LoggerFactory.getLogger(ReportGenerator.class);
    String generateReport(Project project, String fileName) throws FileNotFoundException;
    default File prepareFileForReport(String fileName) {
        if (!Paths.get(fileName).getParent().toFile().exists()) {
            try {
                Files.createDirectories(Paths.get(fileName).getParent());
            } catch (IOException e) {
                LOG.error(String.format("Could not create directories: %s", Paths.get(fileName).getParent()));
                throw new RuntimeException("Could not create directories.");
            }
        }
        return new File(fileName);
    }
}
