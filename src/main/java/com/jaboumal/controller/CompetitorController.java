package com.jaboumal.controller;

import com.jaboumal.constants.EventMessages;
import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.CompetitorSelectionDialog;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.gui.MainFrame;
import com.jaboumal.services.BarcodeCreatorService;
import com.jaboumal.services.FileReaderService;
import com.jaboumal.services.PDFService;
import com.jaboumal.services.PrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Competitor
 * This class is responsible for loading the competitor data from the file, searching for a competitor and creating a standblatt
 * The class also contains a method to add the competitor data to the fields in the main frame and show a message
 * The class also contains a method to search for a competitor with a license number
 * The class also contains a method to search for a competitor with a search text
 *
 * @author Malo Jaboulet
 */
public class CompetitorController {
    private static final Logger log = LoggerFactory.getLogger(CompetitorController.class);
    private static List<CompetitorDTO> competitors;
    private static File printRecordFile;


    /**
     * Load the competitors from the file
     * If the file is not found or the format is wrong, an error message is displayed
     */
    public static void loadCompetitorsFromFile() {
        FileReaderService fileReaderService = new FileReaderService();
        try {
            competitors = fileReaderService.readCompetitorFile();
        } catch (IllegalArgumentException ex) {
            EventMessagePanel.addErrorMessage(EventMessages.COMPETITOR_FILE_WRONG_FORMAT);
            log.error(ex.getMessage());
        }
    }

    /**
     * Add the competitor data to the fields in the main frame and show a message
     * If the competitor is not found, an error message is displayed
     *
     * @param competitor the competitor to add to the fields
     */
    public void addCompetitorDataToFieldsAndShowMessage(CompetitorDTO competitor) {
        if (competitor != null) {
            MainFrame.addCompetitorDataToFields(competitor);
            EventMessagePanel.addSuccessMessage(EventMessages.COMPETITOR_FOUND);

        } else {
            EventMessagePanel.addErrorMessage(EventMessages.NO_COMPETITOR_FOUND);
            MainFrame.addCompetitorDataToFields(null);
        }

    }

    /**
     * Search for a competitor with a license number
     *
     * @param lizenzNummer the license number to search for
     * @return the competitor with the license number
     */
    public CompetitorDTO searchCompetitorWithLizenzNummer(int lizenzNummer) {
        for (CompetitorDTO competitorDTO : competitors) {
            if (competitorDTO.getLizenzNummer() == lizenzNummer) {
                return competitorDTO;
            }
        }
        return null;
    }

    /**
     * Search for a competitor with a search text
     * If the search text is found in the first name, last name or license number of a competitor, the competitor is added to the result list
     * If the result list contains only one competitor, this competitor is returned
     * If the result list contains more than one competitor, a dialog is shown to select the competitor
     * If no competitor is found, a warning message is displayed
     *
     * @param searchText the search text to search for
     * @return the competitor with the search text
     */
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
            CompetitorSelectionDialog competitorSelectionDialog = new CompetitorSelectionDialog();
            //open dialog to select the competitor
            CompetitorDTO selectedCompetitor = competitorSelectionDialog.showDialog(resultList);

            if (selectedCompetitor != null) {
                return selectedCompetitor;
            }
        }

        log.warn("No person found for: {}", searchText);
        return null;
    }

    /**
     * Create a standblatt for a competitor
     * The barcode is created with the license number of the competitor
     * The XML file is created with the first name, last name, date of birth and barcode of the competitor
     * The XML data is loaded in a DOCX file
     * The DOCX file is printed
     *
     * @param competitor the competitor to create the standblatt for
     */
    public static void createStandblattAndPrint(CompetitorDTO competitor) {
        try {
            BarcodeCreatorService barcodeCreatorService = new BarcodeCreatorService();
            String barcode = barcodeCreatorService.createBarcode(competitor.getLizenzNummer());

            PDFService PDFService = new PDFService();
            String pathPrintingFile = PDFService.createPDF(competitor.getFirstName(), competitor.getLastName(), competitor.getDateOfBirth(), barcode, competitor.isGuest());
            PrintService.printDoc(pathPrintingFile);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Create the print record file
     */
    public void createPrintRecordFile(){
        String path = String.format(FilePaths.getPath(FilePaths.OUTPUT_PRINT_RECORD_PATH), LocalDate.now());
        CompetitorController.printRecordFile = FileReaderService.createFile(path);
    }

    /**
     * Write a record to the print record file
     *
     * @param record the record to write
     */
    public static void writeToPrintRecordFile(String record){
        FileReaderService.addDataToFile(printRecordFile, record);
    }

    /**
     * Get the competitors
     *
     * @return the competitors
     */
    public List<CompetitorDTO> getCompetitors() {
        return CompetitorController.competitors;
    }
    /**
     * ONLY FOR TESTING
     * Reset the competitors
     */
    void resetCompetitors() {
        CompetitorController.competitors = null;
    }
}
