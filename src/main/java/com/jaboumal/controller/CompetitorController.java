package com.jaboumal.controller;

import com.jaboumal.constants.EventMessages;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.gui.GuiFrame;
import com.jaboumal.services.PrintService;
import com.jaboumal.services.XMLService;
import com.jaboumal.services.BarcodeCreatorService;
import com.jaboumal.services.FileReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompetitorController {
    private static final Logger log = LoggerFactory.getLogger(CompetitorController.class);
    private static List<CompetitorDTO> competitors;


    public void loadCompetitorsFromFile() {
        FileReaderService fileReaderService = new FileReaderService();
        competitors = fileReaderService.readCompetitorFile();
        log.info("Competitors file read");
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
                log.info("Found person for: {}", searchText);
                return competitorDTO;
            }
        }
        log.warn("No person found for: {}", searchText);
        return null;
    }

    public static void createStandblatt(CompetitorDTO competitor) {
        try {
            BarcodeCreatorService barcodeCreatorService = new BarcodeCreatorService();
            String barcode = barcodeCreatorService.createBarcode(competitor.getLizenzNummer());

            XMLService xmlService = new XMLService();
            xmlService.createXml(competitor.getFirstName() + " " + competitor.getLastName(), competitor.getDateOfBirth(), barcode);

            String fileName = xmlService.loadXMLDataInDocxFile(competitor.getFirstName() + "_" + competitor.getLastName());
            PrintService.printDoc(fileName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
