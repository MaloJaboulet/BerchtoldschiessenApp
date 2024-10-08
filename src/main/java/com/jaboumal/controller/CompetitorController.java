package com.jaboumal.controller;

import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.gui.GuiFrame;
import com.jaboumal.services.PrintService;
import com.jaboumal.util.BarcodeCreator;
import com.jaboumal.util.FileReader;
import com.jaboumal.util.XMLCreate;
import com.jaboumal.util.XMLToDocxLoader;
import com.jaboumal.constants.EventMessages;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class CompetitorController {
    private static final Logger log = LoggerFactory.getLogger(CompetitorController.class);
    private static List<CompetitorDTO> competitors;


    public void loadCompetitorsFromFile() {
        FileReader fileReader = new FileReader();
        competitors = fileReader.readCompetitorFile();
        log.info("Competitors file read");

        /*File folder = new File("src/main/resources/");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    System.out.println("File " + listOfFile.getName());
                } else if (listOfFile.isDirectory()) {
                    System.out.println("Directory " + listOfFile.getName());
                }
            }
        }*/
    }

    public static void addCompetitorDataToFieldsAndShowMessage(CompetitorDTO competitor) {

        if (competitor != null) {
            GuiFrame.addCompetitorDataToFields(competitor);
            EventMessagePanel.addSuccessMessage(EventMessages.COMPETITOR_FOUND);

        } else {
            EventMessagePanel.addErrorMessage(EventMessages.NO_COMPETITOR_FOUND);
            GuiFrame.addCompetitorDataToFields(null);
        }

    }

    public static CompetitorDTO searchCompetitorWithLizenzNummer(int lizenzNummer) {
        for (CompetitorDTO competitorDTO : competitors) {
            if (competitorDTO.getLizenzNummer() == lizenzNummer) {
                return competitorDTO;
            }
        }
        return null;
    }

    public static CompetitorDTO searchCompetitorWithSearchText(String searchText) {
        for (CompetitorDTO competitorDTO : competitors) {
            if (competitorDTO.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                    competitorDTO.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
                    String.valueOf(competitorDTO.getLizenzNummer()).equals(searchText)) {
                return competitorDTO;
            }
        }
        log.warn("No person found for: {}", searchText);
        return null;
    }

    public static void createStandblatt(CompetitorDTO competitor) {
        try {
            BarcodeCreator barcodeCreator = new BarcodeCreator();
            String barcode = barcodeCreator.createBarcode(competitor.getLizenzNummer());

            XMLCreate xmlCreate = new XMLCreate();
            xmlCreate.createXml(competitor.getFirstName() + " " + competitor.getLastName(), competitor.getDateOfBirth(), barcode);

            XMLToDocxLoader loader = new XMLToDocxLoader();
            String fileName = loader.loadDataInDocxFile(competitor.getFirstName() + "_" + competitor.getLastName());
            PrintService.printDoc(fileName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
