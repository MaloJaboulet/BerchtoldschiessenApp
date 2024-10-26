package com.jaboumal.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.okapibarcode.backend.Code128;
import uk.org.okapibarcode.backend.HumanReadableLocation;
import uk.org.okapibarcode.graphics.Color;
import uk.org.okapibarcode.output.Java2DRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

/**
 * Service class for creating barcodes based on a given Schuetzen number.
 *
 * @author Malo Jaboulet
 */
public class BarcodeCreatorService {
    private static final Logger log = LoggerFactory.getLogger(BarcodeCreatorService.class);

    /**
     * Generates a barcode for the given Schuetzen number, converts it to a Base64-encoded string.
     *
     * @param schuetzenNummer the Schuetzen number for which the barcode is to be created
     * @return a Base64-encoded string representation of the generated barcode image
     * @throws UncheckedIOException if an I/O error occurs during barcode image creation
     */
    public String createBarcode(int schuetzenNummer) {

        int newSchuetzenNummer = ((schuetzenNummer + 10_000_000) * 100);
        int rest = newSchuetzenNummer % 97;
        int barcodeNummer = (97 - rest) + newSchuetzenNummer;

        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();

            ImageIO.write(createBarcodeImage(barcodeNummer), "png", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (final IOException ioe) {
            log.error(ioe.getMessage(), ioe);
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * Creates a barcode image for the given Schuetzen number.
     *
     * @param schuetzenNummer the Schuetzen number for which the barcode is to be created
     * @return a BufferedImage representation of the generated barcode image
     */
    private BufferedImage createBarcodeImage(int schuetzenNummer) {
        Code128 barcode = new Code128();
        barcode.setFontName("Monospaced");
        barcode.setFontSize(16);
        barcode.setModuleWidth(2);
        barcode.setBarHeight(50);
        barcode.setHumanReadableLocation(HumanReadableLocation.BOTTOM);
        barcode.setContent(String.valueOf(schuetzenNummer));

        int width = barcode.getWidth();
        int height = barcode.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = image.createGraphics();
        Java2DRenderer renderer = new Java2DRenderer(g2d, 1, uk.org.okapibarcode.graphics.Color.WHITE, Color.BLACK);
        renderer.render(barcode);
        g2d.dispose();

        return image;
    }
}
