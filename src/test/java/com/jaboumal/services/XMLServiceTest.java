package com.jaboumal.services;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.util.ConfigUtil;
import jakarta.xml.bind.JAXBException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static com.jaboumal.constants.FilePaths.INPUT_XML_PATH;
import static org.junit.jupiter.api.Assertions.*;

class XMLServiceTest {

    private XMLService xmlService;

    @BeforeEach
    void setUp() {
        ConfigUtil.loadConfigFile();
        xmlService = new XMLService();
    }

    @Test
    void createXml() throws JAXBException {
        xmlService.createXml("Hans_Peter", "2000-01-01", "123456789");

        File file = new File(FilePaths.getPath(INPUT_XML_PATH));

        assertNotNull(file);
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void loadXMLDataInDocxFile() throws JAXBException, FileNotFoundException, Docx4JException {
        xmlService.createXml("Hans_Peter", "2000-01-01", "123456789");

        String outputDocxPath = xmlService.loadXMLDataInDocxFile("Hans_Peter");

        assertEquals("src/test/resources/output/Berchtoldschiessen_Hans_Peter.docx", outputDocxPath);
        File file = new File(outputDocxPath);

        assertNotNull(file);
        assertTrue(file.exists());

        file.delete();
    }
}