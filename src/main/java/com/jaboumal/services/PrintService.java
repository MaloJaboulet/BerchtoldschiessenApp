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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class for printing files
 *
 * @author Malo Jaboulet
 */
public class PrintService {
    private static final Logger log = LoggerFactory.getLogger(PrintService.class);
    // Thread Pool für die Hintergrundausführung
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Asynchronously print the file at the given path
     *
     * @param pathsPrintingFile the path of the file to print
     */
    public static void printDoc(List<String> pathsPrintingFile) {
        executorService.submit(() -> {
            try {
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                pras.add(MediaSizeName.ISO_A4);
                pras.add(OrientationRequested.PORTRAIT);

                javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

                log.debug("Default Print Service: {}", defaultService);
                if (defaultService != null) {
                    for (String pathPrintingFile : pathsPrintingFile) {
                        if (pathPrintingFile == null) {
                            continue;
                        }

                        File outputFile = new File(pathPrintingFile);

                        DocPrintJob job = defaultService.createPrintJob();
                        FileInputStream input = new FileInputStream(outputFile);
                        Doc doc = new SimpleDoc(input, DocFlavor.INPUT_STREAM.PNG, null);

                        pras.add(new JobName(outputFile.getName(), null));
                        job.print(doc, pras);

                        log.info("Printing file: {}", outputFile.getName());
                        input.close();
                        outputFile.delete();

                        String record = LocalDateTime.now() + "," + outputFile.getName() + "\n";
                        CompetitorController.writeToPrintRecordFile(record);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to print file: {}", pathsPrintingFile, e);
            }
        });
    }

    /**
     * Shutdown the executor service
     */
    public static void shutdown() {
        executorService.shutdown();
    }
}