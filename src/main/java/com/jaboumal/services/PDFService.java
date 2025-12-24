package com.jaboumal.services;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.BerchtoldschiessenDTO;
import com.jaboumal.util.DateUtil;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Base64;

import static com.jaboumal.constants.FilePaths.INPUT_GEWEHR_PDF_PATH;
import static com.jaboumal.constants.FilePaths.INPUT_PISTOLE_PDF_PATH;
import static com.jaboumal.constants.FilePaths.OUTPUT_FILE_PATH;

/**
 * PDFService class
 * This class provides methods to create PDF files from templates and convert them to images.
 * It uses the iText and PDFBox libraries to manipulate PDF files.
 * The PDF files are created from templates and filled with data from the DTOs.
 * The PDF files are then converted to images for printing.
 * The images are saved in the output directory.
 * The class is used by the CompetitorController to generate PDF files for competitors
 * participating in the Berchtoldschiessen.
 * The class is also used by the PrintService to print the generated PDF files.
 **/
public class PDFService {
    private static final Logger log = LoggerFactory.getLogger(PDFService.class);


    /**
     * Create a PDF file from the Gewehr template and convert it to an image
     *
     * @param firstName   the first name of the competitor
     * @param lastName    the last name of the competitor
     * @param dateOfBirth the date of birth of the competitor
     * @param barcode     the barcode of the competitor
     * @param isGuest     whether the competitor is a guest
     * @return the path of the image file
     */
    public String createPDFGewehr(String firstName, String lastName, LocalDate dateOfBirth, String barcode, boolean isGuest) {
        BerchtoldschiessenDTO berchtoldschiessenDTO = new BerchtoldschiessenDTO(firstName, lastName, dateOfBirth, barcode, null, isGuest, !isGuest);
        String imageOutputPath = null;
        try {
            String pdfOutputPath = String.format(FilePaths.getPath(OUTPUT_FILE_PATH) + ".pdf", firstName + "_" + lastName, 1);
            // Step 1: Load the PDF document
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(FilePaths.getPath(INPUT_GEWEHR_PDF_PATH)), new PdfWriter(pdfOutputPath));

            // Step 2: Get the AcroForm
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            Document document = new Document(pdfDoc);

            // Step 3: Locate the form field (example field name: "imageField")
            PdfFormField field = form.getField("barcode");
            form.getField("firstName").setValue(berchtoldschiessenDTO.firstName());
            form.getField("lastName").setValue(berchtoldschiessenDTO.lastName());
            form.getField("geburtsdatum").setValue(getBirthYear(berchtoldschiessenDTO.geburtsdatum()));
            form.getField("datum").setValue(DateUtil.dateToString(berchtoldschiessenDTO.datum()));
            form.getField("istGast").setValue(String.valueOf(berchtoldschiessenDTO.istGast()));
            form.getField("istAktiv").setValue(String.valueOf(berchtoldschiessenDTO.istAktiv()));
            //Need to be there, if not the checkboxes are checked flipped
            form.getField("istGast").setValue(berchtoldschiessenDTO.istGast() ? "On" : "Off");
            form.getField("istAktiv").setValue(berchtoldschiessenDTO.istAktiv() ? "On" : "Off");

            addImageToForm(field, berchtoldschiessenDTO, document, form);

            form.flattenFields();
            // Step 5: Close the document
            document.close();
            pdfDoc.close();
            log.debug("Pdf saved");

            imageOutputPath = pdfToImage(pdfOutputPath, firstName, lastName, 1);

            File file = new File(pdfOutputPath);
            file.delete();
            log.debug("Pdf deleted");

        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }

        return imageOutputPath;
    }


    /**
     * Create a PDF file from the Pistole template and convert it to an image
     *
     * @param firstName   the first name of the competitor
     * @param lastName    the last name of the competitor
     * @param dateOfBirth the date of birth of the competitor
     * @param isGuest     whether the competitor is a guest
     * @return the path of the image file
     */
    public String createPDFPistole(String firstName, String lastName, LocalDate dateOfBirth, boolean isGuest) {
        BerchtoldschiessenDTO berchtoldschiessenDTO = new BerchtoldschiessenDTO(firstName, lastName, dateOfBirth, null, null, isGuest, !isGuest);
        String imageOutputPath = null;
        try {
            String pdfOutputPath = String.format(FilePaths.getPath(OUTPUT_FILE_PATH) + ".pdf", firstName + "_" + lastName, 2);
            // Step 1: Load the PDF document
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(FilePaths.getPath(INPUT_PISTOLE_PDF_PATH)), new PdfWriter(pdfOutputPath));

            // Step 2: Get the AcroForm
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            Document document = new Document(pdfDoc);

            form.getField("firstName").setValue(berchtoldschiessenDTO.firstName());
            form.getField("lastName").setValue(berchtoldschiessenDTO.lastName());
            form.getField("geburtsdatum").setValue(getBirthYear(berchtoldschiessenDTO.geburtsdatum()));
            form.getField("firstName2").setValue(berchtoldschiessenDTO.firstName());
            form.getField("lastName2").setValue(berchtoldschiessenDTO.lastName());
            form.getField("geburtsdatum2").setValue(getBirthYear(berchtoldschiessenDTO.geburtsdatum()));


            form.getField("istGast").setValue(String.valueOf(berchtoldschiessenDTO.istGast()));
            form.getField("istAktiv").setValue(String.valueOf(berchtoldschiessenDTO.istAktiv()));
            //Need to be there, if not the checkboxes are checked flipped
            form.getField("istGast").setValue(berchtoldschiessenDTO.istGast() ? "On" : "Off");
            form.getField("istAktiv").setValue(berchtoldschiessenDTO.istAktiv() ? "On" : "Off");
            form.getField("istGast2").setValue(String.valueOf(berchtoldschiessenDTO.istGast()));
            form.getField("istAktiv2").setValue(String.valueOf(berchtoldschiessenDTO.istAktiv()));
            form.getField("istGast2").setValue(berchtoldschiessenDTO.istGast() ? "On" : "Off");
            form.getField("istAktiv2").setValue(berchtoldschiessenDTO.istAktiv() ? "On" : "Off");

            form.flattenFields();
            // Step 5: Close the document
            document.close();
            pdfDoc.close();
            log.debug("Pdf saved");

            imageOutputPath = pdfToImage(pdfOutputPath, firstName, lastName, 2);

            //TODO remove when pdf is vertical
            BufferedImage originalImg = ImageIO.read(new File(imageOutputPath));
            BufferedImage rotatedImg = rotateClockwise90(originalImg);
            ImageIO.write(rotatedImg, "PNG", new File(imageOutputPath));

            File file = new File(pdfOutputPath);
            file.delete();
            log.debug("Pdf deleted");

        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }

        return imageOutputPath;
    }

    /**
     * Add the image to the form
     *
     * @param field                 the form field
     * @param berchtoldschiessenDTO the DTO
     * @param document              the document
     * @param form                  the form
     */
    private void addImageToForm(PdfFormField field, BerchtoldschiessenDTO berchtoldschiessenDTO, Document document, PdfAcroForm form) {
        if (field != null) {
            // Step 4: Replace field with an image
            ImageData imageData = ImageDataFactory.create(Base64.getDecoder().decode(berchtoldschiessenDTO.barcode()));
            Image image = new Image(imageData);

            // Optional: Adjust image position and size
            image.setHeight(45);
            image.setWidth(170);
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Get field rectangle and add the image
            com.itextpdf.kernel.geom.Rectangle rect = field.getWidgets().getFirst().getRectangle().toRectangle();

            image.setFixedPosition(rect.getLeft(), rect.getBottom(), rect.getWidth());
            document.add(image);

            // Remove the original field after placing the image
            form.removeField("barcode");

        } else {
            log.error("Field 'imageField' not found.");
        }
    }

    /**
     * Convert the PDF file to an image
     *
     * @param path      the path of the PDF file
     * @param firstName the first name of the competitor
     * @param lastName  the last name of the competitor
     */
    private String pdfToImage(String path, String firstName, String lastName, int number) {
        //replace all é, è, à, etc. with e, a, etc.
        firstName = Normalizer.normalize(firstName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        firstName = firstName.replaceAll(" ", "_");
        lastName = Normalizer.normalize(lastName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        lastName = lastName.replaceAll(" ", "_");

        String outputImagePath = String.format(FilePaths.getPath(OUTPUT_FILE_PATH), firstName + "_" + lastName, number) + ".png";
        try (PDDocument document = Loader.loadPDF(new File(path))) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File outputFile = new File(outputImagePath);
                ImageIO.write(image, "PNG", outputFile);
            }

            log.debug("Pdf converted to image");
        } catch (IOException e) {
            log.error("Could not convert pdf to image: {}", e.getMessage());
        }

        return outputImagePath;
    }

    private String getBirthYear(LocalDate birthday) {
        String birthdayStr = DateUtil.dateToString(birthday);
        return birthdayStr.substring(birthdayStr.length() - 4);
    }

    private BufferedImage rotateClockwise90(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(h, w, src.getType());
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                dest.setRGB(y, w - x - 1, src.getRGB(x, y));
        return dest;
    }

}
