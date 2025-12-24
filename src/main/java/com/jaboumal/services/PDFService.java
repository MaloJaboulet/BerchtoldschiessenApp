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

    private static final int GEWEHR_TYPE = 1;
    private static final int PISTOLE_TYPE = 2;
    private static final int IMAGE_DPI = 300;
    private static final String IMAGE_FORMAT = "PNG";
    private static final String PDF_EXTENSION = ".pdf";
    private static final String IMAGE_EXTENSION = ".png";

    private static final int BARCODE_HEIGHT = 45;
    private static final int BARCODE_WIDTH = 170;

    private static final String FIELD_BARCODE = "barcode";
    private static final String FIELD_FIRST_NAME = "firstName";
    private static final String FIELD_LAST_NAME = "lastName";
    private static final String FIELD_BIRTH_DATE = "geburtsdatum";
    private static final String FIELD_DATE = "datum";
    private static final String FIELD_IS_GUEST = "istGast";
    private static final String FIELD_IS_ACTIVE = "istAktiv";
    private static final String CHECKBOX_ON = "On";
    private static final String CHECKBOX_OFF = "Off";


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
        BerchtoldschiessenDTO dto = new BerchtoldschiessenDTO(firstName, lastName, dateOfBirth, barcode, null, isGuest, !isGuest);
        return createPDF(dto, INPUT_GEWEHR_PDF_PATH, GEWEHR_TYPE, true);
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
        BerchtoldschiessenDTO dto = new BerchtoldschiessenDTO(firstName, lastName, dateOfBirth, null, null, isGuest, !isGuest);
        return createPDF(dto, INPUT_PISTOLE_PDF_PATH, PISTOLE_TYPE, false);
    }

    /**
     * Common method to create a PDF from a template and convert it to an image
     *
     * @param dto            the data transfer object containing competitor information
     * @param templatePath   the path to the PDF template
     * @param documentType   the type of document (GEWEHR_TYPE or PISTOLE_TYPE)
     * @param includeBarcode whether to include a barcode in the document
     * @return the path of the generated image file, or null if an error occurred
     */
    private String createPDF(BerchtoldschiessenDTO dto, String templatePath, int documentType, boolean includeBarcode) {
        String pdfOutputPath = null;
        String imageOutputPath;

        try {
            pdfOutputPath = generatePdfPath(dto.firstName(), dto.lastName(), documentType);

            PdfDocument pdfDoc = new PdfDocument(
                    new PdfReader(FilePaths.getPath(templatePath)),
                    new PdfWriter(pdfOutputPath)
            );

            try (Document document = new Document(pdfDoc)) {
                PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

                if (documentType == GEWEHR_TYPE) {
                    populateGewehrForm(form, dto, document, includeBarcode);
                } else if (documentType == PISTOLE_TYPE) {
                    populatePistoleForm(form, dto);
                }

                form.flattenFields();
            }

            pdfDoc.close();
            log.debug("PDF saved: {}", pdfOutputPath);

            imageOutputPath = convertPdfToImage(pdfOutputPath, dto.firstName(), dto.lastName(), documentType);

            if (documentType == PISTOLE_TYPE) {
                rotateImageClockwise90(imageOutputPath);
            }

            return imageOutputPath;

        } catch (IOException e) {
            log.error("Error creating PDF for {} {}: {}", dto.firstName(), dto.lastName(), e.getMessage(), e);
            return null;
        } finally {
            deletePdfFile(pdfOutputPath);
        }
    }

    /**
     * Populate the Gewehr form with data from the DTO
     *
     * @param form           the PDF form to populate
     * @param dto            the data transfer object
     * @param document       the PDF document
     * @param includeBarcode whether to include a barcode
     */
    private void populateGewehrForm(PdfAcroForm form, BerchtoldschiessenDTO dto, Document document, boolean includeBarcode) {
        setFormFieldValue(form, FIELD_FIRST_NAME, dto.firstName());
        setFormFieldValue(form, FIELD_LAST_NAME, dto.lastName());
        setFormFieldValue(form, FIELD_BIRTH_DATE, getBirthYear(dto.geburtsdatum()));
        setFormFieldValue(form, FIELD_DATE, DateUtil.dateToString(dto.datum()));
        setCheckboxField(form, FIELD_IS_GUEST, dto.istGast());
        setCheckboxField(form, FIELD_IS_ACTIVE, dto.istAktiv());

        if (includeBarcode) {
            PdfFormField barcodeField = form.getField(FIELD_BARCODE);
            addBarcodeToForm(barcodeField, dto, document, form);
        }
    }

    /**
     * Populate the Pistole form with data from the DTO
     *
     * @param form the PDF form to populate
     * @param dto  the data transfer object
     */
    private void populatePistoleForm(PdfAcroForm form, BerchtoldschiessenDTO dto) {
        String birthYear = getBirthYear(dto.geburtsdatum());

        // First section
        setFormFieldValue(form, FIELD_FIRST_NAME, dto.firstName());
        setFormFieldValue(form, FIELD_LAST_NAME, dto.lastName());
        setFormFieldValue(form, FIELD_BIRTH_DATE, birthYear);
        setCheckboxField(form, FIELD_IS_GUEST, dto.istGast());
        setCheckboxField(form, FIELD_IS_ACTIVE, dto.istAktiv());

        // Second section (duplicate fields with "2" suffix)
        setFormFieldValue(form, FIELD_FIRST_NAME + "2", dto.firstName());
        setFormFieldValue(form, FIELD_LAST_NAME + "2", dto.lastName());
        setFormFieldValue(form, FIELD_BIRTH_DATE + "2", birthYear);
        setCheckboxField(form, FIELD_IS_GUEST + "2", dto.istGast());
        setCheckboxField(form, FIELD_IS_ACTIVE + "2", dto.istAktiv());
    }

    /**
     * Set a text field value in the form
     *
     * @param form      the PDF form
     * @param fieldName the name of the field
     * @param value     the value to set
     */
    private void setFormFieldValue(PdfAcroForm form, String fieldName, String value) {
        PdfFormField field = form.getField(fieldName);
        if (field != null) {
            field.setValue(value);
        } else {
            log.warn("Form field '{}' not found", fieldName);
        }
    }

    /**
     * Set a checkbox field value in the form
     *
     * @param form      the PDF form
     * @param fieldName the name of the field
     * @param checked   whether the checkbox should be checked
     */
    private void setCheckboxField(PdfAcroForm form, String fieldName, boolean checked) {
        PdfFormField field = form.getField(fieldName);
        if (field != null) {
            // Need to set twice to avoid checkbox being flipped
            field.setValue(String.valueOf(checked));
            field.setValue(checked ? CHECKBOX_ON : CHECKBOX_OFF);
        } else {
            log.warn("Checkbox field '{}' not found", fieldName);
        }
    }

    /**
     * Add a barcode image to the form
     *
     * @param field    the form field where the barcode should be placed
     * @param dto      the data transfer object containing the barcode
     * @param document the PDF document
     * @param form     the PDF form
     */
    private void addBarcodeToForm(PdfFormField field, BerchtoldschiessenDTO dto, Document document, PdfAcroForm form) {
        if (field == null) {
            log.error("Barcode field not found");
            return;
        }

        if (dto.barcode() == null || dto.barcode().isEmpty()) {
            log.warn("Barcode data is empty");
            return;
        }

        try {
            ImageData imageData = ImageDataFactory.create(Base64.getDecoder().decode(dto.barcode()));
            Image image = new Image(imageData);

            image.setHeight(BARCODE_HEIGHT);
            image.setWidth(BARCODE_WIDTH);
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);

            com.itextpdf.kernel.geom.Rectangle rect = field.getWidgets().getFirst().getRectangle().toRectangle();
            image.setFixedPosition(rect.getLeft(), rect.getBottom(), rect.getWidth());

            document.add(image);
            form.removeField(FIELD_BARCODE);
        } catch (Exception e) {
            log.error("Error adding barcode to form: {}", e.getMessage(), e);
        }
    }

    /**
     * Generate the PDF output path
     *
     * @param firstName    the first name
     * @param lastName     the last name
     * @param documentType the document type
     * @return the PDF output path
     */
    private String generatePdfPath(String firstName, String lastName, int documentType) {
        String sanitizedName = sanitizeName(firstName) + "_" + sanitizeName(lastName);
        return String.format(FilePaths.getPath(OUTPUT_FILE_PATH) + PDF_EXTENSION, sanitizedName, documentType);
    }

    /**
     * Sanitize a name for use in a filename
     *
     * @param name the name to sanitize
     * @return the sanitized name
     */
    private String sanitizeName(String name) {
        if (name == null || name.isEmpty()) {
            return "unknown";
        }
        // Remove diacritics (é, è, à, etc.)
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String ascii = normalized.replaceAll("[^\\p{ASCII}]", "");
        // Replace spaces with underscores
        return ascii.replaceAll(" ", "_");
    }

    /**
     * Convert a PDF file to a PNG image
     *
     * @param pdfPath      the path to the PDF file
     * @param firstName    the first name
     * @param lastName     the last name
     * @param documentType the document type
     * @return the path to the generated image
     */
    private String convertPdfToImage(String pdfPath, String firstName, String lastName, int documentType) {
        String sanitizedFirstName = sanitizeName(firstName);
        String sanitizedLastName = sanitizeName(lastName);
        String outputImagePath = String.format(
                FilePaths.getPath(OUTPUT_FILE_PATH),
                sanitizedFirstName + "_" + sanitizedLastName,
                documentType
        ) + IMAGE_EXTENSION;

        try (PDDocument document = Loader.loadPDF(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, IMAGE_DPI, ImageType.RGB);
                File outputFile = new File(outputImagePath);
                ImageIO.write(image, IMAGE_FORMAT, outputFile);
            }

            log.debug("PDF converted to image: {}", outputImagePath);
        } catch (IOException e) {
            log.error("Could not convert PDF to image: {}", e.getMessage(), e);
        }

        return outputImagePath;
    }

    /**
     * Rotate an image clockwise by 90 degrees
     *
     * @param imagePath the path to the image file
     */
    private void rotateImageClockwise90(String imagePath) {
        try {
            BufferedImage originalImg = ImageIO.read(new File(imagePath));
            BufferedImage rotatedImg = rotateImageClockwise90(originalImg);
            ImageIO.write(rotatedImg, IMAGE_FORMAT, new File(imagePath));
            log.debug("Image rotated: {}", imagePath);
        } catch (IOException e) {
            log.error("Could not rotate image: {}", e.getMessage(), e);
        }
    }

    /**
     * Rotate a BufferedImage clockwise by 90 degrees
     *
     * @param src the source image
     * @return the rotated image
     */
    private BufferedImage rotateImageClockwise90(BufferedImage src) {
        int originalWidth = src.getWidth();
        int originalHeight = src.getHeight();

        // When rotating 90 degrees clockwise, dimensions swap
        BufferedImage dest = new BufferedImage(originalHeight, originalWidth, src.getType());

        for (int y = 0; y < originalHeight; y++) {
            for (int x = 0; x < originalWidth; x++) {
                // Rotate clockwise 90°: (x, y) -> (height - 1 - y, x)
                dest.setRGB(originalHeight - 1 - y, x, src.getRGB(x, y));
            }
        }

        return dest;
    }

    /**
     * Extract the birth year from a date
     *
     * @param birthday the birthday date
     * @return the birth year as a string
     */
    private String getBirthYear(LocalDate birthday) {
        if (birthday == null) {
            return "";
        }
        String birthdayStr = DateUtil.dateToString(birthday);
        return birthdayStr.substring(birthdayStr.length() - 4);
    }

    /**
     * Delete a PDF file
     *
     * @param pdfPath the path to the PDF file
     */
    private void deletePdfFile(String pdfPath) {
        if (pdfPath == null) {
            return;
        }

        try {
            File file = new File(pdfPath);
            if (file.exists() && !file.delete()) {
                log.warn("Could not delete temporary PDF file: {}", pdfPath);
            } else {
                log.debug("Temporary PDF deleted: {}", pdfPath);
            }
        } catch (Exception e) {
            log.warn("Error deleting temporary PDF file {}: {}", pdfPath, e.getMessage());
        }
    }
}
