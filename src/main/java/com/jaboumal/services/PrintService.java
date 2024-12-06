package com.jaboumal.services;

import com.jaboumal.controller.CompetitorController;
import org.docx4j.Docx4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Service class for printing files
 *
 * @author Malo Jaboulet
 */
public class PrintService {
    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    /**
     * Print the file at the given path
     *
     * @param pathPrintingFile the path of the file to print
     */
    public static void printDoc(String pathPrintingFile) {
        String docxPath = pathPrintingFile;
        String pdfPath = pathPrintingFile.replace(".docx", ".pdf");

        FileReaderService.convertDocxToPdf(docxPath, pdfPath);
        File wordFile = new File(docxPath);
        wordFile.delete();
        File outputFile = new File(pdfPath);

        try {
            javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

            log.debug("Default Print Service: {}", defaultService);
            if (defaultService != null) {

                Desktop.getDesktop().print(outputFile);
                outputFile.delete();


                String record = LocalDateTime.now() + "," + outputFile.getName() + "\n";
                CompetitorController.writeToPrintRecordFile(record);
            }

        } catch (Exception e) {
            log.error("Failed to print file: {}", outputFile.getName(), e);
        }
    }
}
