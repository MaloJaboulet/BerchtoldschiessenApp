package com.jaboumal.services;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

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
        String baseDir = "src/test/resources/";
        System.setProperty(BASE_DIRECTORY, baseDir);
        System.setProperty(INPUT_FOLDER, "input/");
        System.setProperty(OUTPUT_FOLDER, "output/");
        System.setProperty(INPUT_COMPETITORS, "competitors.csv");
        System.setProperty(INPUT_DOCX, "Berchtoldschiessen_test2.docx");
        System.setProperty(INPUT_XML, "data_output.xml");
        System.setProperty(OUTPUT_DOCX, "Berchtoldschiessen_%s.docx");
        System.setProperty("app.env", "test");
        FilePaths.loadPaths();
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
}