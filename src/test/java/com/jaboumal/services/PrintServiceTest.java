package com.jaboumal.services;

import com.jaboumal.controller.CompetitorController;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled("Will print a real file")
@ExtendWith(MockitoExtension.class)
class PrintServiceTest {

    @Mock
    private javax.print.PrintService printServiceMock;

    @Mock
    private DocPrintJob docPrintJobMock;

    @InjectMocks
    private PrintService printService;

    @BeforeEach
    void setUp() throws IOException {
        copyTestFiles();
        MockitoAnnotations.openMocks(this);
        CompetitorController competitorController = new CompetitorController();
        competitorController.createPrintRecordFile();
    }

    @Test
    void testPrintDocSuccessful() throws Exception {
        try (MockedStatic<PrintServiceLookup> printServiceLookupMockedStatic = mockStatic(PrintServiceLookup.class)) {
            // Mock PrintServiceLookup to return a valid PrintService
            printServiceLookupMockedStatic.when(PrintServiceLookup::lookupDefaultPrintService).thenReturn(printServiceMock);


            // Mock the print job creation
            when(printServiceMock.createPrintJob()).thenReturn(docPrintJobMock);


            // Act
            String pathPrintingFile = "src/test/resources/output/Berchtoldschiessen_Hans_Peter.docx";
            PrintService.printDoc(Collections.singletonList(pathPrintingFile));

            // Assert
            ArgumentCaptor<Doc> docCaptor = ArgumentCaptor.forClass(Doc.class);
            ArgumentCaptor<PrintRequestAttributeSet> prasCaptor = ArgumentCaptor.forClass(PrintRequestAttributeSet.class);

            // Verify that print() was called on docPrintJobMock
            verify(docPrintJobMock).print(docCaptor.capture(), prasCaptor.capture());

            // Verify the captured arguments
            Doc doc = docCaptor.getValue();
            assertTrue(doc.getPrintData() instanceof InputStream); // InputStream should be captured

            PrintRequestAttributeSet pras = prasCaptor.getValue();
            assertEquals(1, pras.size());
            assertTrue(pras.containsKey(JobName.class));
        }
    }

    @Test
    void testPrintDocWhenNoPrintService() {
        // Arrange
        String pathPrintingFile = "src/test/resources/output/Berchtoldschiessen_Hans_Peter.docx";

        try (MockedStatic<PrintServiceLookup> printServiceLookupMockedStatic = mockStatic(PrintServiceLookup.class)) {
            // Mock PrintServiceLookup to return a valid PrintService
            printServiceLookupMockedStatic.when(PrintServiceLookup::lookupDefaultPrintService).thenReturn(null);
            // Act
            assertDoesNotThrow(() -> PrintService.printDoc(Collections.singletonList(pathPrintingFile)));
        }
    }

    @Test
    void testPrintDocWithException() {
        // Act
        assertDoesNotThrow(() ->PrintService.printDoc(Collections.singletonList("BlaBlaBla")));

    }


    private void copyTestFiles() throws IOException {
        File original = new File("src/test/resources/testFiles/Berchtoldschiessen_Hans_Peter.docx");
        File copied = new File(
                "src/test/resources/output/Berchtoldschiessen_Hans_Peter.docx");
        FileUtils.copyFile(original, copied);
    }
}

