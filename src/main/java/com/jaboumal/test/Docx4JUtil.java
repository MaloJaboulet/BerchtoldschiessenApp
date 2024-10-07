package com.jaboumal.test;

import org.apache.commons.io.FilenameUtils;
import org.docx4j.TextUtils;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.fields.ComplexFieldLocator;
import org.docx4j.model.fields.FieldRef;
import org.docx4j.model.fields.FieldsPreprocessor;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.contenttype.ContentTypes;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.ImageBmpPart;
import org.docx4j.openpackaging.parts.WordprocessingML.ImageJpegPart;
import org.docx4j.openpackaging.parts.WordprocessingML.ImagePngPart;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Docx4JUtil {


    static String DOC_INPUT = "src/main/resources/Berchtoldschiessen_test.docx";
    static String DOC_OUTPUT = "src/main/resources/output/Berchtoldschiessen_test.docx";
    static String PDF_OUTPUT = "src/main/resources/output/Berchtoldschiessen_test.pdf";
    static String BARCODE_INPUT = "src/main/resources/barcode.png";

    public static void main(String[] args) throws Exception {
        Docx4JUtil docx4JUtil = new Docx4JUtil();

        String document = DOC_INPUT;
        FileInputStream fis = new FileInputStream(document);
        final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fis);


        docx4JUtil.replaceImageById("rId6", "barcode.png", new File(BARCODE_INPUT), wordMLPackage);

        File outputFile = new File(DOC_OUTPUT);
        wordMLPackage.save(outputFile);
        //docx4JUtil.com.jaboumal.test();
    }

    void test() {

        String template = DOC_INPUT;
        try (FileInputStream fis = new FileInputStream(template)) {
            final WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fis);

            List<FieldRef> fieldRefs = findAllFieldRefs(wordMLPackage);

            FieldRef field = findMergeField(wordMLPackage, fieldRefs, "${schuetzenNummer}");
            byte[] bytes = loadImageFromFile();
            Inline inline = newImage(wordMLPackage, bytes);
            org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
            org.docx4j.wml.Drawing drawing = factory.createDrawing();
            drawing.getAnchorOrInline().add(inline);

            org.docx4j.wml.R run = factory.createR();
            run.getContent().add(drawing);

            //This does not work, either adding drawing directly or via a Run
            //field.getParent().getContent().add(drawing);

            //this works and adds image to end of document
            // wordMLPackage.getMainDocumentPart().getContent().add(drawing);

            //Find P in MainDocumentPart
            for (Object o : wordMLPackage.getMainDocumentPart().getContent()) {
                if (o instanceof P) {
                    P mainP = (P) o;
                    if (TextUtils.getText(mainP).contains("${schuetzenNummer}")) {
                        //Check if the field P matches that in the com.jaboumal.Main document - DOES NOT
                        //System.out.println("Field P = MainDocumentP: " + (((P) field.getParent()) == mainP));

                        //This will add the image at the correct location
                        mainP.getContent().add(drawing);
                        break;
                    }
                }
            }

            File file = new File(DOC_OUTPUT);
            wordMLPackage.save(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FieldRef findMergeField(final WordprocessingMLPackage wordMLPackage, List<FieldRef> fieldRefs, final String fieldDescription) {
        for (FieldRef fr : fieldRefs) {
            for (Object o : fr.getInstructions()) {
                final Object obj = XmlUtils.unwrap(o);
                if (obj instanceof Text) {
                    String instr = ((Text) obj).getValue();
                    if (instr.contains("MERGE") && instr.contains(fieldDescription)) {
                        return fr;
                    }
                }
            }
        }
        return null;

    }

    private List<FieldRef> findAllFieldRefs(final WordprocessingMLPackage wordMLPackage) {
        List<Object> contentList = wordMLPackage.getMainDocumentPart().getContent();
        ComplexFieldLocator fl = new ComplexFieldLocator();
        new TraversalUtil(contentList, fl);

        // canonicalise and setup fieldRefs
        List<FieldRef> fieldRefs = new ArrayList<>();
        for (P p : fl.getStarts()) {
            int index = ((ContentAccessor) p.getParent()).getContent().indexOf(p);
            P newP = FieldsPreprocessor.canonicalise(p, fieldRefs);
            ((ContentAccessor) p.getParent()).getContent().set(index, newP);
        }
        return fieldRefs;
    }

    private static void recurse(List<String> mergeFields, FieldRef fr, String indent) {

        for (Object o : fr.getInstructions()) {
            if (o instanceof FieldRef) {
                recurse(mergeFields, ((FieldRef) o), indent + "    ");
            } else {
                o = XmlUtils.unwrap(o);
                if (o instanceof Text) {
                    String instr = ((Text) o).getValue();
                    if (instr.contains("MERGE")) {
                        mergeFields.add(instr);
                    }
                } else {
                    System.out.println(indent + XmlUtils.unwrap(o).getClass().getName());
                }
            }
        }

    }

    public Inline newImage(WordprocessingMLPackage wordMLPackage,
                           byte[] bytes) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        return imagePart.createImageInline("", "", 0, 1, false);

    }

    private byte[] loadImageFromFile() {
        final File f = new File(BARCODE_INPUT);

        try (InputStream is = new FileInputStream(f)) {
            long length = f.length();
            // You cannot create an array using a long type.
            // It needs to be an int type.
            if (length > Integer.MAX_VALUE) {
                System.out.println("File too large!!");
            }
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                System.out.println("Could not completely read file " + f.getName());
            }

            return bytes;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to open image file");
        }

    }

    public void replaceImageById(String id, String placeholderImageName, File newImage, WordprocessingMLPackage document) throws Exception {
        Relationship rel = document.getMainDocumentPart().getRelationshipsPart().getRelationshipByID(id);

        BinaryPartAbstractImage imagePart = null;
        if (FilenameUtils.getExtension(placeholderImageName).toLowerCase().equals(ContentTypes.EXTENSION_BMP)) {
            imagePart = new ImageBmpPart(new PartName("/word/media/" + placeholderImageName));
        } else if (ContentTypes.EXTENSION_JPG_1.contains(FilenameUtils.getExtension(placeholderImageName).toLowerCase())) {
            imagePart = new ImageJpegPart(new PartName("/word/media/" + placeholderImageName));
        } else if (FilenameUtils.getExtension(placeholderImageName).toLowerCase().equals(ContentTypes.EXTENSION_PNG)) {
            imagePart = new ImagePngPart(new PartName("/word/media/" + placeholderImageName));
        }

        InputStream stream = new FileInputStream(newImage);
        imagePart.setBinaryData(stream);

        if (FilenameUtils.getExtension(newImage.getName()).toLowerCase().equals(ContentTypes.EXTENSION_BMP)) {
            imagePart.setContentType(new ContentType(ContentTypes.IMAGE_BMP));
        } else if (ContentTypes.EXTENSION_JPG_1.
                contains(FilenameUtils.getExtension(newImage.getName()).toLowerCase())) {
            imagePart.setContentType(new ContentType(ContentTypes.IMAGE_JPEG));
        } else if (FilenameUtils.getExtension(newImage.getName()).toLowerCase().equals(ContentTypes.EXTENSION_PNG)) {
            imagePart.setContentType(new ContentType(ContentTypes.IMAGE_PNG));
        }

        imagePart.setRelationshipType(Namespaces.IMAGE);

        final String embedId = rel.getId();

        rel = document.getMainDocumentPart().addTargetPart(imagePart);
        rel.setId(embedId);
    }
}
