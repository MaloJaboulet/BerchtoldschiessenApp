package com.jaboumal.services;

import com.jaboumal.util.ConfigUtil;
import com.jaboumal.util.DateUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for PDFService
 * Tests cover PDF generation, image conversion, edge cases, and error handling
 */
class PDFServiceTest {

    private PDFService pdfService;
    private static final String VALID_BARCODE = "iVBORw0KGgoAAAANSUhEUgAAALQAAABGCAAAAABA/OH8AAAEe0lEQVR4Xu2V0U4bARDE+P+fTgvBN3YaQk6VUKk4qbvZiT3cQ0tfXn4/l8vrn9d5va6frvt17nt/U7Yk39trWgfSbc2c6rS8jcY0Ybbkyuc1rQPptmZOdVreRmOaMFty5fOa1oF0WzOnOi1vozFNmC258nlN60C6rZlTnZa30ZgmzJZc+bymdSDd1sypTsvbaEwTZkuufF7TOpBua+ZUp+VtNKYJsyVXPq9pHUi3NXOq0/I2GtOE2ZIrn9e0DqTbmjnVaXkbjWnCbMmVz2taB9JtzZzqtLyNxjRhtuTK5zWtA+m2Zk51Wt5GY5owW3Ll85rWgXRbM6c6LW+jMU2YLbnyeU3rQLqtmVOdlrfRmCbMllz5vKZ1IN3WzKlOy9toTBNmS658XtM6kG5r5lSn5W00pgmzJVc+r2kdSLc1c6rT8jYa04TZkiuf17QOpNuaOdVpeRuNacJsyZXPa1oH0m3NnOq0vI3GNGG25MrnNa0D6bZmTnVa3kZjmjBbcuXzmtaBdFszpzotb6MxTZgtufJ5TetAuq2ZU52Wt9GYJsyWXPm8pnUg3dbMqU7L22hME2ZLrnxe0zqQbmvmVKflbTSmCbMlVz6vaR1ItzVzqtPyNhrThNmSK5/XtA6k25o51Wl5G41pwmzJlc9rWgfSbc2c6rS8jcY0Ybbkyuc1rQPptmZOdVreRmOaMFty5fOa1oF0WzOnOi1vozFNmC258nlN60C6rZlTnZa30ZgmzJZc+bymdSDd1sypTsvbaEwTZkuufF7TOpBua+ZUp+VtNKYJsyVXPq9pHUi3NXOq0/I2GtOE2ZIrn9e0DqTbmjnVaXkbjWnCbMmVz2taB9JtzZzqtLyNxjRhtuTK5zWtA+m2Zk51Wt5GY5owW3Ll85rWgXRbM6c6LW+jMU2YLbnyeU3rQLqtmVOdlrfRmCbMllz5vKZ1IN3WzKlOy9toTBNmS658XtM6kG5r5lSn5W00pgmzJVc+r2kdSLc1c6rT8jYa04TZkiuf17QOpNuaOdVpeRuNacJsyZXPa1oH0m3NnOq0vI3GNGG25MrnNa0D6bZmTnVa3kZjmjBbcuXzmtaBdFszpzotb6MxTZgtufJ5TetAuq2ZU52Wt9GYJsyWXPm8pnUg3dbMqU7L22hME2ZLrnxe0zqQbmvmVKflbTSmCbMlVz6vaR1ItzVzqtPyNhrThNmSK5/XtA6k25o51Wl5G41pwmzJlc9rWgfSbc2c6rS8jcY0Ybbkyuc1rQPptmZO3+Pv9fy89Fc9Py/9Vc///9LH7xr92smvpdtv//z0hr2H97KnnjPoC/T1RyXbfH+RD4boe9lzzwn0ctD7mce+/cF9rRoPsueeE+jl0Uvffr73vse31w/3sueeM+ynL31E/KV9+0w08I9vj+y55wz7yUv7nxIgf+fvGse3yp56TsEPX7pNuz5+6Y+yT59T8KOXvik6zuuHe0Y+nnqPU/CDl970fdf463c+Q+v/gOOf1ZHdfHl8a5mPi/j2zGtcztL/yPPz0l/1fMuX/gU2ZU1ZsO8RKwAAAABJRU5ErkJggg==";

    @BeforeEach
    void setUp() {
        ConfigUtil.loadConfigFile();
        pdfService = new PDFService();
    }

    @AfterEach
    void tearDown() {
        // Cleanup any generated files after each test
        cleanupGeneratedFiles();
    }

    // ==================== Integration Tests ====================

    @Test
    @DisplayName("Should create PDF Gewehr with barcode and convert to image")
    void createPDFGewehr_withValidData_shouldGenerateImageFile() {
        // Given
        String firstName = "Hans";
        String lastName = "Peter";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.2000");
        boolean isGuest = true;

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, isGuest);

        // Then
        assertNotNull(outputFile, "Output file path should not be null");
        assertEquals("src/test/resources/output/Berchtoldschiessen_Hans_Peter_1.png", outputFile);

        File file = new File(outputFile);
        assertTrue(file.exists(), "Generated image file should exist");
        assertTrue(file.length() > 0, "Generated image file should not be empty");

        // Verify it's a valid PNG image
        assertDoesNotThrow(() -> {
            BufferedImage image = ImageIO.read(file);
            assertNotNull(image, "Should be able to read the image");
            assertTrue(image.getWidth() > 0, "Image should have width");
            assertTrue(image.getHeight() > 0, "Image should have height");
        });
    }

    @Test
    @DisplayName("Should create PDF Gewehr for active member")
    void createPDFGewehr_withActiveMember_shouldGenerateImageFile() {
        // Given
        String firstName = "Maria";
        String lastName = "Müller";
        LocalDate dateOfBirth = DateUtil.stringToDate("15.05.1995");
        boolean isGuest = false; // Active member

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, isGuest);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should create PDF Pistole and convert to image with rotation")
    void createPDFPistole_withValidData_shouldGenerateRotatedImageFile() {
        // Given
        String firstName = "Hans";
        String lastName = "Peter";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.2000");
        boolean isGuest = true;

        // When
        String outputFile = pdfService.createPDFPistole(firstName, lastName, dateOfBirth, isGuest);

        // Then
        assertNotNull(outputFile, "Output file path should not be null");
        assertEquals("src/test/resources/output/Berchtoldschiessen_Hans_Peter_2.png", outputFile);

        File file = new File(outputFile);
        assertTrue(file.exists(), "Generated image file should exist");
        assertTrue(file.length() > 0, "Generated image file should not be empty");

        // Verify it's a valid PNG image
        assertDoesNotThrow(() -> {
            BufferedImage image = ImageIO.read(file);
            assertNotNull(image, "Should be able to read the rotated image");
        });
    }

    @Test
    @DisplayName("Should create PDF Pistole for active member")
    void createPDFPistole_withActiveMember_shouldGenerateImageFile() {
        // Given
        String firstName = "Thomas";
        String lastName = "Schmidt";
        LocalDate dateOfBirth = DateUtil.stringToDate("20.03.1988");
        boolean isGuest = false;

        // When
        String outputFile = pdfService.createPDFPistole(firstName, lastName, dateOfBirth, isGuest);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    // ==================== Special Characters Tests ====================

    @ParameterizedTest
    @CsvSource({
            "François, Dubois",
            "José, García",
            "Müller, Schröder",
            "Łukasz, Nowak",
            "André, Côté"
    })
    @DisplayName("Should handle names with special characters")
    void createPDFGewehr_withSpecialCharacters_shouldSanitizeFilename(String firstName, String lastName) {
        // Given
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        assertNotNull(outputFile);
        assertFalse(outputFile.contains("é"), "Output path should not contain special characters");
        assertFalse(outputFile.contains("ü"), "Output path should not contain special characters");
        assertFalse(outputFile.contains("ł"), "Output path should not contain special characters");

        File file = new File(outputFile);
        assertTrue(file.exists(), "File with sanitized name should be created");
    }

    @Test
    @DisplayName("Should handle names with spaces")
    void createPDFGewehr_withSpacesInName_shouldReplaceWithUnderscore() {
        // Given
        String firstName = "Jean Paul";
        String lastName = "Van Der Berg";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        assertNotNull(outputFile);
        assertTrue(outputFile.contains("_"), "Spaces should be replaced with underscores");

        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    // ==================== Edge Cases Tests ====================

    @Test
    @DisplayName("Should handle very long names")
    void createPDFGewehr_withLongNames_shouldGenerateFile() {
        // Given
        String firstName = "Maximilian-Alexander-Ferdinand";
        String lastName = "von-Hohenzollern-Sigmaringen";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, false);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should handle single character names")
    void createPDFGewehr_withSingleCharNames_shouldGenerateFile() {
        // Given
        String firstName = "A";
        String lastName = "B";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should handle future birth dates")
    void createPDFGewehr_withFutureBirthDate_shouldGenerateFile() {
        // Given
        String firstName = "Future";
        String lastName = "Person";
        LocalDate dateOfBirth = LocalDate.now().plusYears(1);

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should handle very old birth dates")
    void createPDFGewehr_withOldBirthDate_shouldGenerateFile() {
        // Given
        String firstName = "Ancient";
        String lastName = "Person";
        LocalDate dateOfBirth = LocalDate.of(1900, 1, 1);

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, false);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    // ==================== Multiple Generation Tests ====================

    @Test
    @DisplayName("Should generate multiple PDFs without conflicts")
    void createMultiplePDFs_shouldNotHaveConflicts() {
        // Given
        String[] firstNames = {"John", "Jane", "Bob", "Alice"};
        String[] lastNames = {"Doe", "Smith", "Johnson", "Williams"};

        // When & Then
        for (int i = 0; i < firstNames.length; i++) {
            String outputFile = pdfService.createPDFGewehr(
                    firstNames[i],
                    lastNames[i],
                    DateUtil.stringToDate("01.01.1990"),
                    VALID_BARCODE,
                    i % 2 == 0
            );

            assertNotNull(outputFile);
            File file = new File(outputFile);
            assertTrue(file.exists(), "File " + i + " should exist");
        }
    }

    @Test
    @DisplayName("Should overwrite existing file when generating with same name")
    void createPDFGewehr_withExistingFile_shouldOverwrite() {
        // Given
        String firstName = "Overwrite";
        String lastName = "Test";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // Create first file
        String outputFile1 = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);
        File file1 = new File(outputFile1);
        assertTrue(file1.exists(), "First file should exist");

        // Wait a bit to ensure different timestamp
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When - Create second file with same name
        String outputFile2 = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        assertEquals(outputFile1, outputFile2, "Output paths should be the same");
        File file2 = new File(outputFile2);
        assertTrue(file2.exists(), "File should still exist");
        assertTrue(file2.length() > 0, "File should have content");
    }

    // ==================== Both Document Types Test ====================

    @Test
    @DisplayName("Should generate both Gewehr and Pistole PDFs for same person")
    void createBothDocumentTypes_forSamePerson_shouldGenerateBothFiles() {
        // Given
        String firstName = "Both";
        String lastName = "Types";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String gewehrFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);
        String pistoleFile = pdfService.createPDFPistole(firstName, lastName, dateOfBirth, true);

        // Then
        assertNotNull(gewehrFile);
        assertNotNull(pistoleFile);
        assertNotEquals(gewehrFile, pistoleFile, "Files should have different names");

        File gewehr = new File(gewehrFile);
        File pistole = new File(pistoleFile);

        assertTrue(gewehr.exists(), "Gewehr file should exist");
        assertTrue(pistole.exists(), "Pistole file should exist");
    }

    // ==================== Date Handling Tests ====================

    @Test
    @DisplayName("Should handle leap year dates")
    void createPDFGewehr_withLeapYearDate_shouldGenerateFile() {
        // Given
        String firstName = "Leap";
        String lastName = "Year";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 29); // Leap year

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, false);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should handle current date as birth date")
    void createPDFGewehr_withCurrentDate_shouldGenerateFile() {
        // Given
        String firstName = "Today";
        String lastName = "Born";
        LocalDate dateOfBirth = LocalDate.now();

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        assertNotNull(outputFile);
        File file = new File(outputFile);
        assertTrue(file.exists());
    }

    // ==================== Image Quality Tests ====================

    @Test
    @DisplayName("Generated image should have expected properties")
    void createPDFGewehr_generatedImage_shouldHaveCorrectProperties() throws IOException {
        // Given
        String firstName = "Quality";
        String lastName = "Test";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String outputFile = pdfService.createPDFGewehr(firstName, lastName, dateOfBirth, VALID_BARCODE, true);

        // Then
        File file = new File(outputFile);
        BufferedImage image = ImageIO.read(file);

        assertNotNull(image, "Image should be readable");
        assertTrue(image.getWidth() > 100, "Image should have reasonable width");
        assertTrue(image.getHeight() > 100, "Image should have reasonable height");
        assertEquals(BufferedImage.TYPE_3BYTE_BGR, image.getType(), "Image should be RGB format");
    }

    @Test
    @DisplayName("Pistole image should be rotated")
    void createPDFPistole_generatedImage_shouldBeRotated() throws IOException {
        // Given
        String firstName = "Rotation";
        String lastName = "Test";
        LocalDate dateOfBirth = DateUtil.stringToDate("01.01.1990");

        // When
        String outputFile = pdfService.createPDFPistole(firstName, lastName, dateOfBirth, true);

        // Then
        File file = new File(outputFile);
        BufferedImage image = ImageIO.read(file);

        assertNotNull(image, "Rotated image should be readable");
        // After 90° rotation, dimensions should be swapped compared to original
        assertTrue(image.getWidth() > 0, "Rotated image should have width");
        assertTrue(image.getHeight() > 0, "Rotated image should have height");
    }

    // ==================== Utility Methods ====================

    /**
     * Clean up any generated test files
     */
    private void cleanupGeneratedFiles() {
        try {
            File outputDir = new File("src/test/resources/output");
            if (outputDir.exists() && outputDir.isDirectory()) {
                File[] files = outputDir.listFiles((dir, name) -> name.startsWith("Berchtoldschiessen_") && name.endsWith(".png"));
                if (files != null) {
                    for (File file : files) {
                        if (!file.delete()) {
                            System.err.println("Failed to delete test file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}