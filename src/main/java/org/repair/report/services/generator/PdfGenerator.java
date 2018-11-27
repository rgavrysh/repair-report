package org.repair.report.services.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.repair.model.Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class PdfGenerator implements ReportGenerator {
    @Override
    public String generateReport(Project project, String fileName) throws FileNotFoundException {
        AtomicInteger counter = new AtomicInteger(1);
        Document document = new Document();
        File file = prepareFileForReport(fileName);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (DocumentException | FileNotFoundException e) {
            LOG.error("Failed to generate pdf.");
            return null;
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        document.addTitle(project.getAddress().getStreet() + " / " + project.getAddress().getStreetNumber());
        Chunk chunk = new Chunk(project.getAddress().getStreet(), font);

        PdfPTable jobTable = new PdfPTable(5);

        Stream.of("#", "description", "tariff", "qty", "total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    jobTable.addCell(header);
                });

        project.getTasks().entrySet().stream().forEach(entry -> {
            PdfPCell num = new PdfPCell(Phrase.getInstance(String.valueOf(counter.getAndIncrement())));
            num.setHorizontalAlignment(Element.ALIGN_CENTER);
            num.setVerticalAlignment(Element.ALIGN_CENTER);
            num.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            jobTable.addCell(num);

            PdfPCell desc = new PdfPCell(Phrase.getInstance(entry.getKey().getShortDescription()));
            desc.setHorizontalAlignment(Element.ALIGN_CENTER);
            desc.setVerticalAlignment(Element.ALIGN_CENTER);
            desc.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            jobTable.addCell(desc);

            jobTable.addCell(entry.getKey().getTariff().toString());
            jobTable.addCell(entry.getValue().toString());
            jobTable.addCell(String.valueOf(entry.getKey().getTariff() * entry.getValue()));

        });

        try {
            document.add(chunk);
            document.add(jobTable);
        } catch (DocumentException e) {
            LOG.error("Failed to add element to pdf document.");
            return null;
        }

        document.close();
        return file.getAbsolutePath();
    }
}