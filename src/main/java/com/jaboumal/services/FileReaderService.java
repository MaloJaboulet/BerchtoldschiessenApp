package com.jaboumal.services;

import com.jaboumal.constants.EventMessages;
import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS;

public class FileReaderService {
    private final static Logger log = LoggerFactory.getLogger(FileReaderService.class);

    public List<CompetitorDTO> readCompetitorFile() {
        List<CompetitorDTO> competitors = new ArrayList<>();
        try {
            FilePaths filePaths = new FilePaths();
            File competitorFile = new File(filePaths.getPath(INPUT_COMPETITORS));
            Scanner competitorScanner = new Scanner(competitorFile);//macht einen neuen Scanner


            while (competitorScanner.hasNextLine()) {
                List<String> row = getRecordFromLine(competitorScanner.nextLine());
                if (row.size() != 4) {
                    throw new IllegalArgumentException("competitor.csv does not contain 4 columns.");
                }
                int lizenzNummer = Integer.parseInt(row.get(0));
                String firstname = row.get(1);
                String lastname = row.get(2);
                String dateOfBirth = row.get(3);

                CompetitorDTO competitorDTO = new CompetitorDTO(lizenzNummer, firstname, lastname, dateOfBirth);
                competitors.add(competitorDTO);
            }
            competitorScanner.close();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            EventMessagePanel.addErrorMessage(EventMessages.NO_COMPETITOR_FILE_FOUND);
        }

        log.info("Competitors file read");
        return competitors;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}