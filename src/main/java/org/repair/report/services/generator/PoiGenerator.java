package org.repair.report.services.generator;

import org.apache.poi.xwpf.usermodel.*;
import org.repair.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PoiGenerator implements ReportGenerator {

    private final Logger logger = LoggerFactory.getLogger(PoiGenerator.class);

    @Override
    public String generateReport(Project project, String fileName) {
        final String colorBlack = "000000";

        AtomicInteger counter = new AtomicInteger(1);
        File file = new File(fileName);
        try (XWPFDocument document = new XWPFDocument(); FileOutputStream report = new FileOutputStream(file)) {


            // Set header
            XWPFParagraph header = document.createParagraph();
            header.createRun().setText(project.getAddress().getStreet() + " / " + project.getAddress().getStreetNumber());

            // Create table
            XWPFTable table = document.createTable(1, 5);
            table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(400));
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(6000));
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1500));
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1500));
            table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(1500));

            table.setWidthType(TableWidthType.PCT);
            XWPFTableRow tableHeader = table.getRow(0);
            tableHeader.getCell(0).setText("#");
            tableHeader.getCell(1).setText("Job Description");
            tableHeader.getCell(2).setText("tariff");
            tableHeader.getCell(3).setText("qty");
            tableHeader.getCell(4).setText("total");

            project.getTasks().forEach((job, qty) -> {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(counter.getAndIncrement()));
                row.getCell(1).setText(job.getShortDescription());
                row.getCell(2).setText(String.valueOf(job.getTariff()));
                row.getCell(3).setText(String.valueOf(qty));
                row.getCell(4).setText(String.valueOf(job.getTariff() * qty));
            });

            table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 7, 5, colorBlack);
            table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 7, 5, colorBlack);
            table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 7, 5, colorBlack);
            table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 7, 5, colorBlack);
            table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 5, 5, colorBlack);
            table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 5, 5, colorBlack);


            // set footer
            XWPFParagraph footer = document.createParagraph();
            footer.createRun().setText(project.getClientName() + "\tcell: " + project.getClientPhone());
            document.write(report);
            return file.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Failed to generate POI document.");
            return null;
        }
    }
}
