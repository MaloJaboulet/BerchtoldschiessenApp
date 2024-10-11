package com.jaboumal.services;

import com.jaboumal.dto.CompetitorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.jaboumal.constants.FilePaths.*;
import static org.junit.jupiter.api.Assertions.*;


class FileReaderServiceTest {

    FileReaderService fileReaderService;

    @BeforeEach
    void setUp() {
        fileReaderService = new FileReaderService();
        String baseDir = "src/test/resources/";
        System.setProperty("BASE_DIRECTORY", baseDir);
        System.setProperty(INPUT_COMPETITORS, "input/competitors.csv");
        System.setProperty(INPUT_DOCX, "Berchtoldschiessen_test2.docx");
        System.setProperty(INPUT_XML, "output/data_output.xml");
        System.setProperty(OUTPUT_DOCX, "output/Berchtoldschiessen_%s.docx");
        System.setProperty("app.env", "test");
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
        assertDoesNotThrow(() -> {
            List<CompetitorDTO> result = fileReaderService.readCompetitorFile();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        });
    }

    @Test
    void wrongFileLayout_readCompetitorFile_throwException() {
        System.setProperty(INPUT_COMPETITORS, "input/competitors_wrong.csv");
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> fileReaderService.readCompetitorFile());
        assertEquals("competitor.csv does not contain 4 columns.", throwable.getMessage());
    }
}