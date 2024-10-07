package com.jaboumal.services;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import java.io.File;
import java.io.FileInputStream;

public class PrintService {
    public static void printDoc(String filename) throws Exception {
        File outputFile = new File(filename);
        DocFlavor psFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        //aset.add(Finishings.STAPLE);
        /*PrintService[] pservices = PrintServiceLookup.lookupPrintServices(psFlavor, aset);
        for (PrintService ps : pservices) {
            if (ps.getName().contains("OfficeJet")) {
                DocPrintJob pj = ps.createPrintJob();
                aset.add(new Copies(2));
                aset.add(MediaSizeName.ISO_A4);
                aset.add(Sides.TWO_SIDED_LONG_EDGE);

                try {
                    FileInputStream fis = new FileInputStream(outputFile);
                    Doc doc = new SimpleDoc(fis, psFlavor, null);
                    pj.print(doc, aset);
                    System.out.println("printing");
                } catch (IOException | PrintException e) {
                    System.err.println(e);
                }
                break;
            }
        }
         */

        try {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            //pras.add(OrientationRequested.PORTRAIT);
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            javax.print.PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
            javax.print.PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
            //PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);

            if (defaultService != null) {
                DocPrintJob job = defaultService.createPrintJob();
                FileInputStream input = new FileInputStream(outputFile);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(input, flavor, das);

                pras.add(new JobName(outputFile.getName(), null));
                job.print(doc, pras);
                System.out.println("Printing file: " + outputFile.getName());

            }
        } catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }
}
