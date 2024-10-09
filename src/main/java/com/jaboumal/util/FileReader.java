package com.jaboumal.util;

import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.constants.FilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS;

public class FileReader {
    private final static Logger log = LoggerFactory.getLogger(FileReader.class);

    public List<CompetitorDTO> readCompetitorFile() {
        List<CompetitorDTO> competitors = new ArrayList<>();
        try {
            FilePaths filePaths = new FilePaths();
            File competitorFile = new File(filePaths.getPath(INPUT_COMPETITORS));
            Scanner competitorScanner = new Scanner(competitorFile);//macht einen neuen Scanner


            while (competitorScanner.hasNextLine()) {
                List<String> row = getRecordFromLine(competitorScanner.nextLine());
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
        }

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
