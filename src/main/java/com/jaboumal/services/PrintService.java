package com.jaboumal.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import java.io.File;
import java.io.FileInputStream;

public class PrintService {
    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    public static void printDoc(String filename) {
        File outputFile = new File(filename);

        try {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

            log.debug("Default Print Service: {}", defaultService);
            if (defaultService != null) {
                DocPrintJob job = defaultService.createPrintJob();
                FileInputStream input = new FileInputStream(outputFile);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(input, flavor, das);


                pras.add(new JobName(outputFile.getName(), null));
                //TODO enable printing
                job.print(doc, pras);
                log.info("Printing file: {}", outputFile.getName());
                input.close();
                outputFile.delete();
            }

        } catch (Exception e) {
            log.error("Failed to print file: {}", outputFile.getName(), e);
        }


    }
}
