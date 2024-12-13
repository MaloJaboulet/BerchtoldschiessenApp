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
import com.jaboumal.dto.xml.BerchtoldschiessenDTO;
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
import java.time.LocalDate;
import java.util.Base64;

import static com.jaboumal.constants.FilePaths.INPUT_PDF_PATH;
import static com.jaboumal.constants.FilePaths.OUTPUT_FILE_PATH;

/**
 * Service class for creating and loading XML files
 *
 * @author Malo Jaboulet
 */
public class PDFService {
    private static final Logger log = LoggerFactory.getLogger(PDFService.class);

    /**
     * Create an XML file with the given name, date of birth and barcode
     *
     * @param firstName   the first name of the competitor
     * @param lastName    the last name of the competitor
     * @param dateOfBirth the date of birth of the competitor
     * @param barcode     the barcode of the competitor
     * @param isGuest     if the competitor is a guest
     * @return the path of the output file
     */
    public String createPDF(String firstName, String lastName, LocalDate dateOfBirth, String barcode, boolean isGuest) {
        BerchtoldschiessenDTO berchtoldschiessenDTO = new BerchtoldschiessenDTO(barcode, dateOfBirth, firstName, lastName, !isGuest, isGuest);
        String imageOutputPath = null;
        try {
            String pdfOutputPath = String.format(FilePaths.getPath(OUTPUT_FILE_PATH) + ".pdf", firstName + "_" + lastName);
            // Step 1: Load the PDF document
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(FilePaths.getPath(INPUT_PDF_PATH)), new PdfWriter(pdfOutputPath));

            // Step 2: Get the AcroForm
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            Document document = new Document(pdfDoc);

            // Step 3: Locate the form field (example field name: "imageField")
            PdfFormField field = form.getField("barcode");
            form.getField("firstName").setValue(berchtoldschiessenDTO.getFirstName());
            form.getField("lastName").setValue(berchtoldschiessenDTO.getLastName());
            form.getField("geburtsdatum").setValue(berchtoldschiessenDTO.getGeburtsdatum());
            form.getField("datum").setValue(berchtoldschiessenDTO.getDatum());
            form.getField("istGast").setValue(String.valueOf(berchtoldschiessenDTO.isIstGast()));
            form.getField("istAktiv").setValue(String.valueOf(berchtoldschiessenDTO.isIstAktiv()));

            addImageToForm(field, berchtoldschiessenDTO, document, form);

            form.flattenFields();
            // Step 5: Close the document
            document.close();
            pdfDoc.close();
            log.debug("Pdf saved");

            imageOutputPath = pdfToImage(pdfOutputPath, firstName, lastName);

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
            ImageData imageData = ImageDataFactory.create(Base64.getDecoder().decode(berchtoldschiessenDTO.getBarcode()));
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
    private String pdfToImage(String path, String firstName, String lastName) {
        String outputImagePath = String.format(FilePaths.getPath(OUTPUT_FILE_PATH), firstName + "_" + lastName) + "_%d.png";
        try (PDDocument document = Loader.loadPDF(new File(path))) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                File outputFile = new File(String.format(outputImagePath, page + 1));
                ImageIO.write(image, "PNG", outputFile);
            }

            log.debug("Pdf converted to image");
        } catch (IOException e) {
            log.error("Could not convert pdf to image: {}", e.getMessage());
        }

        return outputImagePath;
    }
}
