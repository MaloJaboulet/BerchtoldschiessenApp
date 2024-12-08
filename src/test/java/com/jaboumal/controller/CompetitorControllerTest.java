
package com.jaboumal.controller;

import com.jaboumal.constants.EventMessages;
import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.CompetitorDTO;
import com.jaboumal.gui.EventMessagePanel;
import com.jaboumal.gui.MainFrame;
import com.jaboumal.services.PrintService;
import com.jaboumal.util.ConfigUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.jaboumal.constants.FilePaths.INPUT_COMPETITORS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CompetitorControllerTest {

    private CompetitorController competitorController;

    private static MockedStatic<EventMessagePanel> eventMessagePanelMock;
    private static MockedStatic<PrintService> printServiceMock;

    @BeforeAll
    static void init() {
        eventMessagePanelMock = mockStatic(EventMessagePanel.class);
        printServiceMock = mockStatic(PrintService.class);
        mockStatic(MainFrame.class);
    }

    @BeforeEach
    void setUp() {
        competitorController = new CompetitorController();
        competitorController.resetCompetitors();
        ConfigUtil.loadConfigFile();
        competitorController.loadCompetitorsFromFile();
    }

    @AfterAll
    static void close() {
        eventMessagePanelMock.close();
        printServiceMock.close();
    }

    @Test
    void testLoadCompetitorsFromFile() {
        List<CompetitorDTO> result = competitorController.getCompetitors();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testLoadCompetitorsFromFileWrongFormat() {
        System.setProperty(INPUT_COMPETITORS, "competitors_wrong.csv");
        FilePaths.loadPaths();
        competitorController.resetCompetitors();

        competitorController.loadCompetitorsFromFile();
        List<CompetitorDTO> result = competitorController.getCompetitors();
        assertNull(result);
        eventMessagePanelMock.verify(() -> EventMessagePanel.addErrorMessage(EventMessages.COMPETITOR_FILE_WRONG_FORMAT), times(1));
    }

    @Test
    void testAddCompetitorDataToFieldsAndShowMessage() {
        CompetitorDTO competitor = new CompetitorDTO();
        competitor.setFirstName("Hans");
        competitor.setLastName("Peter");
        competitor.setLizenzNummer(123456);
        competitorController.addCompetitorDataToFieldsAndShowMessage(competitor);
        eventMessagePanelMock.verify(() -> EventMessagePanel.addSuccessMessage(EventMessages.COMPETITOR_FOUND), times(1));
    }

    @Test
    void testAddCompetitorDataToFieldsAndShowMessageNoCompetitor() {
        competitorController.addCompetitorDataToFieldsAndShowMessage(null);
        eventMessagePanelMock.verify(() -> EventMessagePanel.addErrorMessage(EventMessages.NO_COMPETITOR_FOUND), times(1));
    }

    @Test
    void testSearchCompetitorWithLizenzNummer() {
        CompetitorDTO result = competitorController.searchCompetitorWithLizenzNummer(123456);
        assertNotNull(result);
        assertEquals("Hans", result.getFirstName());
        assertEquals("Peter", result.getLastName());
    }

    @Test
    void testSearchCompetitorWithLizenzNummerNotFound() {
        CompetitorDTO result = competitorController.searchCompetitorWithLizenzNummer(123457);
        assertNull(result);
    }

    @Test
    void testSearchCompetitorWithSearchTextWithFirstName() {
        CompetitorDTO result = CompetitorController.searchCompetitorWithSearchText("Hans");
        assertNotNull(result);
        assertEquals("Hans", result.getFirstName());
        assertEquals("Peter", result.getLastName());
    }

    @Test
    void testSearchCompetitorWithSearchTextWithLastName() {
        CompetitorDTO result = CompetitorController.searchCompetitorWithSearchText("Meier");
        assertNotNull(result);
        assertEquals("Peter", result.getFirstName());
        assertEquals("Meier", result.getLastName());
    }

    @Test
    void testSearchCompetitorWithSearchTextWithLizenzNummer() {
        CompetitorDTO result = CompetitorController.searchCompetitorWithSearchText("123456");
        assertNotNull(result);
        assertEquals("Hans", result.getFirstName());
        assertEquals("Peter", result.getLastName());
    }

    @Test
    void testSearchCompetitorWithSearchTextNotFound() {
        CompetitorDTO result = CompetitorController.searchCompetitorWithSearchText("Heinz");
        assertNull(result);
    }

    @Disabled("Will print a real file")
    @Test
    void testCreateStandblattAndPrint() {
        CompetitorDTO competitor = new CompetitorDTO();
        competitor.setFirstName("Hans");
        competitor.setLastName("Peter");
        competitor.setLizenzNummer(123456);
        competitor.setDateOfBirth(LocalDate.of(2000,1,1));
        CompetitorController.createStandblattAndPrint(competitor);
        printServiceMock.verify(() -> PrintService.printDoc("src/test/resources/output/Berchtoldschiessen_Hans_Peter.docx"), times(1));
    }
}