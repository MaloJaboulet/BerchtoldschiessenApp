package com.jaboumal.services;


import com.jaboumal.constants.EventMessages;
import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS_PATH;

/**
 * Service class for reading files
 *
 * @author Malo Jaboulet
 */
public class FileReaderService {
    private final static Logger log = LoggerFactory.getLogger(FileReaderService.class);

    /**
     * Reads the competitor file from the specified path and parses its content into a list of CompetitorDTO objects.
     * Each line of the file is expected to contain four columns: lizenzNummer, firstname, lastname, dateOfBirth, isGuest.
     * If the file is not found, an error message is logged and displayed.
     *
     * @return a list of CompetitorDTO objects parsed from the competitor file
     * @throws IllegalArgumentException if the file does not contain exactly four columns
     */
    public List<CompetitorDTO> readCompetitorFile() {
        List<CompetitorDTO> competitors = new ArrayList<>();
        try {
            File competitorFile = new File(FilePaths.getPath(INPUT_COMPETITORS_PATH));
            Scanner competitorScanner = new Scanner(competitorFile); // creates a new scanner for the file


            while (competitorScanner.hasNextLine()) {
                List<String> row = getRecordFromLine(competitorScanner.nextLine());
                if (row.size() != 5) {
                    throw new IllegalArgumentException("competitor.csv does not contain 5 columns.");
                }

                int lizenzNummer = Integer.parseInt(row.get(0));
                String firstname = row.get(1);
                String lastname = row.get(2);
                String dateOfBirth = row.get(3);
                boolean isGuest = Boolean.parseBoolean(row.get(4));

                CompetitorDTO competitorDTO = new CompetitorDTO(lizenzNummer, firstname, lastname, DateUtil.stringToDate(dateOfBirth), isGuest);
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

    /**
     * Parses a line of a CSV file into a list of strings.
     *
     * @param line the line to parse
     * @return a list of strings representing the columns of the CSV file
     */
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

    /**
     * Copies a file from the specified path to the specified destination path.
     *
     * @param filePath        the path of the file to copy
     * @param destinationPath the path of the destination file
     */
    public static void copyFile(String filePath, String destinationPath) {
        try (InputStream in = new FileInputStream(filePath)) {
            File targetFile = new File(destinationPath);
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new file with the specified path.
     *
     * @param filePath the path of the file to create
     * @return the created file
     */
    public static File createFile(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * Adds data to a file.
     *
     * @param file the file to add data to
     * @param data the data to add
     */
    public static void addDataToFile(File file, String data) {
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(data);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a file at the specified path.
     *
     * @param filePath the path of the file to delete
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            log.info("File deleted successfully: {}", filePath);
        } else {
            log.error("Failed to delete the file: {}", filePath);
        }
    }
}
