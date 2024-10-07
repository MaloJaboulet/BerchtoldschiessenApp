package com.jaboumal.test;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import uk.org.okapibarcode.backend.Code128;
import uk.org.okapibarcode.backend.HumanReadableLocation;
import uk.org.okapibarcode.graphics.Color;
import uk.org.okapibarcode.output.Java2DRenderer;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

public class WordEditor {
    static String DOC_INPUT = "src/main/resources/Berchtoldschiessen_test.docx";
    static String DOC_OUTPUT = "src/main/resources/output/Berchtoldschiessen_test.docx";
    static String PDF_OUTPUT = "src/main/resources/output/Berchtoldschiessen_test.pdf";
    static String BARCODE_INPUT = "src/main/resources/barcode.png";

    public static void main(String[] args) throws Exception {
        WordEditor wordEditor = new WordEditor();
        // wordEditor.replaceText();

        WordprocessingMLPackage template = WordprocessingMLPackage.load(Files.newInputStream(new File(DOC_INPUT).toPath()));

        // that's it; you can now save `template`, export it as PDF or whatever you want to do
        /*Docx4JSRUtil.searchAndReplace(template, Map.of(
                "${geburtsdatum}", "01.01.2000",
                "${schuetzenNummer}", "1234567",
                "${stich}", "Bal Bla Bla"
        ));*/

        /*File image = new File("barcode.png");
        ImageIO.write(wordEditor.createBarcode(), "png", image);*/

        wordEditor.findNode(template);

        File outputFile = new File(DOC_OUTPUT);

        /*File file = new File("src/main/resources/barcode.png");
        byte[] bytes = wordEditor.convertImageToByteArray(file);
        wordEditor.addImageToPackage(template, bytes);*/

        template.save(outputFile);

        //wordEditor.printDoc(outputFile);

       // outputFile.delete();

        System.out.println("Done");
    }

    public void findNode(WordprocessingMLPackage template){
        System.out.println(template.getMainDocumentPart().getXML()
        );
        /*try {
            //template.getMainDocumentPart().convertAltChunks();

        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }*/


    }

    public void printDoc(File outputFile) throws Exception {
        DocFlavor psFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

        //aset.add(Finishings.STAPLE);
        PrintService[] pservices = PrintServiceLookup.lookupPrintServices(psFlavor,
                aset);
        for (PrintService ps : pservices) {
            if (ps.getName().contains("OfficeJet")) {
                DocPrintJob pj = ps.createPrintJob();
                aset.add(new Copies(2));
                aset.add(MediaSizeName.ISO_A4);
                aset.add(Sides.TWO_SIDED_LONG_EDGE);

                try {
                    FileInputStream fis = new FileInputStream(outputFile);
                    Doc doc = new SimpleDoc(fis, psFlavor, null);
                    pj.print(doc, aset);
                    System.out.println("printing");
                } catch (IOException | PrintException e) {
                    System.err.println(e);
                }
                break;
            }
        }
    }

    public BufferedImage createBarcode() {
        Code128 barcode = new Code128();
        barcode.setFontName("Monospaced");
        barcode.setFontSize(16);
        barcode.setModuleWidth(2);
        barcode.setBarHeight(50);
        barcode.setHumanReadableLocation(HumanReadableLocation.BOTTOM);
        barcode.setContent("12345678");

        int width = barcode.getWidth();
        int height = barcode.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = image.createGraphics();
        Java2DRenderer renderer = new Java2DRenderer(g2d, 1, Color.WHITE, Color.BLACK);
        renderer.render(barcode);
        g2d.dispose();

        return image;
    }

    private void addImageToPackage(WordprocessingMLPackage wordMLPackage,
                                          byte[] bytes) throws Exception {
        BinaryPartAbstractImage imagePart =
                BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        int docPrId = 1;
        int cNvPrId = 2;
        Inline inline = imagePart.createImageInline("Filename hint",
                "Alternative text", docPrId, cNvPrId, false);

        P paragraph = addInlineImageToParagraph(inline);

        wordMLPackage.getMainDocumentPart().addObject(paragraph);
    }

    private P addInlineImageToParagraph(Inline inline) {
        // Now add the in-line image to a paragraph
        ObjectFactory factory = new ObjectFactory();
        P paragraph = factory.createP();
        R run = factory.createR();
        paragraph.getContent().add(run);
        Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return paragraph;
    }


    private byte[] convertImageToByteArray(File file)
            throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        // You cannot create an array using a long, it needs to be an int.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "
                    + file.getName());
        }
        is.close();
        return bytes;
    }
}
