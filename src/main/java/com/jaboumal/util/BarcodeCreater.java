package com.jaboumal.util;

import uk.org.okapibarcode.backend.Code128;
import uk.org.okapibarcode.backend.HumanReadableLocation;
import uk.org.okapibarcode.graphics.Color;
import uk.org.okapibarcode.output.Java2DRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class BarcodeCreater {

    public String createBarcode(int schuetzenNummer) throws IOException {

        int newSchuetzenNummer = ((schuetzenNummer + 10_000_000) * 100);
        int rest = newSchuetzenNummer % 97;
        int barcodeNummer = (97 - rest) + newSchuetzenNummer;

        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();

            //File image = new File(Main.BARCODE_INPUT);
            ImageIO.write(createBarcodeImage(barcodeNummer), "png", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
            /*String barcode = encodeFileToBase64Binary(image);
            System.out.println(barcode);
            return barcode;*/
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

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

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            //encodedfile = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
            encodedfile.replace(" ","");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return encodedfile;
    }
}
