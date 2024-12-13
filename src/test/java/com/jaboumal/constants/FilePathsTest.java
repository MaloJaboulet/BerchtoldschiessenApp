package com.jaboumal.constants;

import com.jaboumal.util.ConfigUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FilePathsTest {

    private FilePaths filePaths;

    @BeforeEach
    void setUp() {
        ConfigUtil.loadConfigFile();
        filePaths = new FilePaths();
    }

    @Test
    void loadPaths() {
        FilePaths.loadPaths();

        Map<String, String> result = filePaths.getPaths();
        assertNotNull(result);
        assertEquals(7, result.size());
        assertEquals("src/test/resources/output/Berchtoldschiessen_%s", result.get(FilePaths.OUTPUT_FILE_PATH));
        assertEquals("src/test/resources/input/competitors.csv", result.get(FilePaths.INPUT_COMPETITORS_PATH));
        assertEquals("src/test/resources/Standbl_Ber.pdf", result.get(FilePaths.INPUT_PDF_PATH));
        assertEquals("src/test/resources/output/Print_Record_Berchtoldschiessen_%s.csv", result.get(FilePaths.OUTPUT_PRINT_RECORD_PATH));
        assertEquals("src/test/resources/", result.get(FilePaths.BASE_DIRECTORY));
        assertEquals("input/", result.get(FilePaths.INPUT_FOLDER));
        assertEquals("output/", result.get(FilePaths.OUTPUT_FOLDER));
    }

    @Test
    void getPathWithKey() {
        FilePaths.loadPaths();

        String result = FilePaths.getPath(FilePaths.OUTPUT_FILE_PATH);
        assertNotNull(result);
        assertEquals("src/test/resources/output/Berchtoldschiessen_%s", result);
    }

    @Test
    void getPathWithKeyNotFound() {
        FilePaths.loadPaths();

        String result = FilePaths.getPath("NOT_FOUND");
        assertNull(result);
    }

    @Test
    void getPathWithKeyNull() {
        FilePaths.loadPaths();

        String result = FilePaths.getPath(null);
        assertNull(result);
    }

    @Test
    void getPathWithKeyEmpty() {
        FilePaths.loadPaths();

        String result = FilePaths.getPath("");
        assertNull(result);
    }

    @Test
    void getPathWithKeyBlank() {
        FilePaths.loadPaths();

        String result = FilePaths.getPath(" ");
        assertNull(result);
    }
}