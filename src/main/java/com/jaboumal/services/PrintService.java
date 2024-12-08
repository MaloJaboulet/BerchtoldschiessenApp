package com.jaboumal.services;

import com.jaboumal.controller.CompetitorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintServiceLookup;
import java.io.File;
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
        File outputFile = new File(pathPrintingFile);

        try {
            javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

            log.debug("Default Print Service: {}", defaultService);
            if (defaultService != null) {

                ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "start", "winword", "/q", "/mFilePrintDefault", "/mFileCloseOrExit ", "/mFileClose", "/mFileExit", outputFile.getAbsolutePath());
                builder.redirectErrorStream(true);
                builder.start();

                outputFile.delete();


                String record = LocalDateTime.now() + "," + outputFile.getName() + "\n";
                CompetitorController.writeToPrintRecordFile(record);
            }

        } catch (Exception e) {
            log.error("Failed to print file: {}", outputFile.getName(), e);
        }
    }
}
