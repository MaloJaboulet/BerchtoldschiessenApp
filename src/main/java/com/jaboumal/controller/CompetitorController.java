package com.jaboumal.controller;

import com.jaboumal.constants.EventMessages;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.CompetitorSelectionDialog;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.gui.MainFrame;
import com.jaboumal.services.BarcodeCreatorService;
import com.jaboumal.services.FileReaderService;
import com.jaboumal.services.PrintService;
import com.jaboumal.services.XMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompetitorController {
    private static final Logger log = LoggerFactory.getLogger(CompetitorController.class);
    private static List<CompetitorDTO> competitors;


    public void loadCompetitorsFromFile() {
        FileReaderService fileReaderService = new FileReaderService();
        try {
            competitors = fileReaderService.readCompetitorFile();
        } catch (IllegalArgumentException ex) {
            EventMessagePanel.addErrorMessage(EventMessages.COMPETITOR_FILE_WRONG_FORMAT);
            log.error(ex.getMessage());
        }
    }

    public static void addCompetitorDataToFieldsAndShowMessage(CompetitorDTO competitor) {

        if (competitor != null) {
            MainFrame.addCompetitorDataToFields(competitor);
            EventMessagePanel.addSuccessMessage(EventMessages.COMPETITOR_FOUND);

        } else {
            EventMessagePanel.addErrorMessage(EventMessages.NO_COMPETITOR_FOUND);
            MainFrame.addCompetitorDataToFields(null);
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
        List<CompetitorDTO> resultList = new ArrayList<>();

        for (CompetitorDTO competitorDTO : competitors) {
            if (competitorDTO.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                    competitorDTO.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
                    String.valueOf(competitorDTO.getLizenzNummer()).equals(searchText)) {
                log.info("Found person for: {}", searchText);
                resultList.add(competitorDTO);
            }
        }

        if (resultList.size() == 1) {
            return resultList.getFirst();
        }

        if (resultList.size() > 1) {
            CompetitorSelectionDialog competitorSelectionDialog =new CompetitorSelectionDialog();
            CompetitorDTO selectedCompetitor = competitorSelectionDialog.showDialog(resultList);

            if (selectedCompetitor != null) {
                return selectedCompetitor;
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

            String pathPrintingFile = xmlService.loadXMLDataInDocxFile(competitor.getFirstName() + "_" + competitor.getLastName());
            PrintService.printDoc(pathPrintingFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
