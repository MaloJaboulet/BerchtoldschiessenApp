package com.jaboumal.util;

import com.jaboumal.constants.FilePaths;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.jaboumal.constants.FilePaths.*;

public class XMLToDocxLoader {
    public String loadDataInDocxFile(String competitorName) throws FileNotFoundException, Docx4JException {
        FilePaths filePaths = new FilePaths();
        String output_docx = String.format(filePaths.getPath(OUTPUT_DOCX), competitorName);

        File inputXml = new File(filePaths.getPath(INPUT_XML));
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(filePaths.getPath(INPUT_DOCX)));
        FileInputStream xmlStream = new FileInputStream(inputXml);
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT);
        Docx4J.save(wordMLPackage, new File(output_docx), Docx4J.FLAG_NONE);
        System.out.println("Saved: " + output_docx);

        inputXml.delete();

        return output_docx;
    }
}
