package com.jaboumal.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
class BarcodeCreatorServiceTest {

    private BarcodeCreatorService barcodeCreatorService;

    @BeforeEach
    void setUp() {
        barcodeCreatorService = new BarcodeCreatorService();
    }

    @Test
    void validSchuetzenNummer_createBarcode_returnBase64String() throws IOException {
        File barcode = new File("src/test/resources/123456_barcode.txt");
        String expectedBarcodeString = Files.readString(Path.of(barcode.getPath()));
        String returnString = barcodeCreatorService.createBarcode(123456);

        assertEquals(expectedBarcodeString, returnString);
    }
}