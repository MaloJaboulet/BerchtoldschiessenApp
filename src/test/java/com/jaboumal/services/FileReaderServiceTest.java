package com.jaboumal.services;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.util.ConfigUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.jaboumal.constants.FilePaths.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class FileReaderServiceTest {

    FileReaderService fileReaderService;
    static MockedStatic<EventMessagePanel> eventMessagePanel;

    @BeforeAll
    static void init() {
        eventMessagePanel = mockStatic(EventMessagePanel.class);
    }

    @AfterAll
    static void close() {
        eventMessagePanel.close();
    }

    @BeforeEach
    void setUp() {
        fileReaderService = new FileReaderService();
        /*String baseDir = "src/test/resources/";
        System.setProperty(BASE_DIRECTORY, baseDir);
        System.setProperty(INPUT_FOLDER, "input/");
        System.setProperty(OUTPUT_FOLDER, "output/");
        System.setProperty(INPUT_COMPETITORS, "competitors.csv");
        System.setProperty(INPUT_DOCX, "Berchtoldschiessen_test2.docx");
        System.setProperty(INPUT_XML, "data_output.xml");
        System.setProperty(OUTPUT_DOCX, "Berchtoldschiessen_%s.docx");
        System.setProperty("app.env", "test");
        FilePaths.loadPaths();*/

        ConfigUtil.loadConfigFile();
    }

    @Test
    void validFile_readCompetitorFile_returnList() {
        List<CompetitorDTO> result = fileReaderService.readCompetitorFile();
        assertNotNull(result);
        assertEquals(2, result.size());

        CompetitorDTO competitorDTO = result.get(0);
        assertNotNull(competitorDTO);
        assertEquals(123456, competitorDTO.getLizenzNummer());
        assertEquals("Hans", competitorDTO.getFirstName());
        assertEquals("Peter", competitorDTO.getLastName());
        assertEquals("01.01.2000", competitorDTO.getDateOfBirth());

        CompetitorDTO competitorDTO2 = result.get(1);
        assertNotNull(competitorDTO2);
        assertEquals(987653, competitorDTO2.getLizenzNummer());
        assertEquals("Peter", competitorDTO2.getFirstName());
        assertEquals("Meier", competitorDTO2.getLastName());
        assertEquals("10.10.1990", competitorDTO2.getDateOfBirth());
    }

    @Test
    void noFile_readCompetitorFile_emptyList() {
        System.setProperty(INPUT_COMPETITORS, "blablabla");
        FilePaths.loadPaths();
        assertDoesNotThrow(() -> {
            List<CompetitorDTO> result = fileReaderService.readCompetitorFile();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        });
    }

    @Test
    void wrongFileLayout_readCompetitorFile_throwException() {
        System.setProperty(INPUT_COMPETITORS, "competitors_wrong.csv");
        FilePaths.loadPaths();
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> fileReaderService.readCompetitorFile());
        assertEquals("competitor.csv does not contain 4 columns.", throwable.getMessage());
    }

    @Test
    void copyFile_validFile_copyFile() {
        String sourcePath = System.getProperty(BASE_DIRECTORY) + System.getProperty(INPUT_FOLDER) + System.getProperty(INPUT_DOCX);
        String destinationPath = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + System.getProperty(INPUT_DOCX);
        assertDoesNotThrow(() -> FileReaderService.copyFile(sourcePath, destinationPath));

        File file = new File(destinationPath);
        assertNotNull(file);
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void copyFile_invalidFile_throwException() {
        String sourcePath = System.getProperty(BASE_DIRECTORY) + System.getProperty(INPUT_FOLDER) + "blablabla";
        String destinationPath = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + "blablabla";
        assertThrows(Exception.class, () -> FileReaderService.copyFile(sourcePath, destinationPath));
    }

    @Test
    void copyFile_invalidDestinationPath_throwException() {
        String sourcePath = System.getProperty(BASE_DIRECTORY) + System.getProperty(INPUT_FOLDER) + System.getProperty(INPUT_DOCX);
        String destinationPath = System.getProperty(BASE_DIRECTORY) + "blablabla" + System.getProperty(OUTPUT_FOLDER) + System.getProperty(INPUT_DOCX);
        assertThrows(RuntimeException.class, () -> FileReaderService.copyFile(sourcePath, destinationPath));
    }

    @Test
    void copyFile_invalidSourcePath_throwException() {
        String sourcePath = System.getProperty(BASE_DIRECTORY) + "blablabla" + System.getProperty(INPUT_FOLDER) + System.getProperty(INPUT_DOCX);
        String destinationPath = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + System.getProperty(INPUT_DOCX);
        assertThrows(RuntimeException.class, () -> FileReaderService.copyFile(sourcePath, destinationPath));
    }

    @Test
    void createFile_validPath_createFile() {
        String path = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + "test.txt";
        File result = FileReaderService.createFile(path);

        assertNotNull(result);
    }

    @Test
    void addDataToFile_validData_addData() {
        String path = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + "test.txt";
        File file = FileReaderService.createFile(path);
        assertNotNull(file);

        String data = "Hello World!";
        FileReaderService.addDataToFile(file, data);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        file.delete();
    }

    @Test
    void addDataToFile_invalidData_throwException() {
        String path = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + "test.txt";
        File file = FileReaderService.createFile(path);
        assertNotNull(file);

        String data = null;
        assertThrows(RuntimeException.class, () -> FileReaderService.addDataToFile(file, data));
        file.delete();
    }

    @Test
    void addTestDataToFile_nullFile_throwException() {
        File file = null;
        String data = "Hello World!";
        assertThrows(RuntimeException.class, () -> FileReaderService.addDataToFile(file, data));
    }

    @Test
    void deleteFile_validPath_deleteFile() {
        String path = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + "test.txt";
        File file = FileReaderService.createFile(path);
        assertNotNull(file);

        FileReaderService.deleteFile(path);
        assertFalse(file.exists());
    }

    @Test
    void deleteFile_invalidPath_throwException() {
        String path = System.getProperty(BASE_DIRECTORY) + System.getProperty(OUTPUT_FOLDER) + "blablabla";
        assertDoesNotThrow(() -> FileReaderService.deleteFile(path));
    }
}