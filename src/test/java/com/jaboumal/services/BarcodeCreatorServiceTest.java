package com.jaboumal.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BarcodeCreatorServiceTest {

    private BarcodeCreatorService barcodeCreatorService;

    @BeforeEach
    void setUp() {
        barcodeCreatorService = new BarcodeCreatorService();
    }

    @Test
    void validSchuetzenNummer_createBarcode_returnValidBase64ImageAndCorrectContent() throws IOException {
        int input = 123456;

        String returnString = barcodeCreatorService.createBarcode(input);
        assertNotNull(returnString, "Returned Base64 string should not be null");
        byte[] imageBytes = Base64.getDecoder().decode(returnString);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            BufferedImage img = ImageIO.read(bais);
            assertNotNull(img, "Decoded Base64 should be a valid image");
            assertTrue(img.getWidth() > 0, "Image width should be > 0");
            assertTrue(img.getHeight() > 0, "Image height should be > 0");
        }
    }


}