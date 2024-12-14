package com.jaboumal.services;

import com.jaboumal.controller.CompetitorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

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
        try {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(MediaSizeName.ISO_A4);
            pras.add(OrientationRequested.PORTRAIT);

            javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();


            log.debug("Default Print Service: {}", defaultService);
            if (defaultService != null) {
                // number of pages in PDF is 2
                for (int i = 1; i == 1; i++) {
                    File outputFile = new File(String.format(pathPrintingFile, i));
                    if (!outputFile.exists()) {
                        continue;
                    }

                    DocPrintJob job = defaultService.createPrintJob();
                    FileInputStream input = new FileInputStream(outputFile);
                    Doc doc = new SimpleDoc(input, DocFlavor.INPUT_STREAM.PNG, null);


                    pras.add(new JobName(outputFile.getName(), null));
                    long start = System.currentTimeMillis();
                    job.print(doc, pras);
                    long end = System.currentTimeMillis();
                    log.info("Printing time: {}", end - start);
                    log.info("Printing file: {}", outputFile.getName());
                    input.close();
                    outputFile.delete();


                    String record = LocalDateTime.now() + "," + outputFile.getName() + "\n";
                    CompetitorController.writeToPrintRecordFile(record);
                }
            }

        } catch (Exception e) {
            log.error("Failed to print file: {}", pathPrintingFile, e);
        }
    }
}
