package com.jaboumal.services;

import com.jaboumal.constants.FilePaths;
import com.jaboumal.dto.BerchtoldschiessenDTO;
import com.jaboumal.dto.RootDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.jaboumal.constants.FilePaths.*;

public class XMLService {
    private static final Logger log = LoggerFactory.getLogger(XMLService.class);

    public void createXml(String name, String dateOfBirth, String barcode) throws JAXBException {
        FilePaths filePaths = new FilePaths();

        BerchtoldschiessenDTO berchtoldschiessenDTO = new BerchtoldschiessenDTO(barcode, dateOfBirth, name);
        RootDTO rootDTO = new RootDTO(berchtoldschiessenDTO);

        File file = new File(filePaths.getPath(INPUT_XML));
        JAXBContext jaxbContext = JAXBContext.newInstance(RootDTO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(rootDTO, file);
    }


    public String loadXMLDataInDocxFile(String competitorName) throws FileNotFoundException, Docx4JException {
        FilePaths filePaths = new FilePaths();
        String output_docx = String.format(filePaths.getPath(OUTPUT_DOCX), competitorName);

        File inputXml = new File(filePaths.getPath(INPUT_XML));
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(filePaths.getPath(INPUT_DOCX)));
        FileInputStream xmlStream = new FileInputStream(inputXml);
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT);
        Docx4J.save(wordMLPackage, new File(output_docx), Docx4J.FLAG_NONE);
        log.info("Saved: {}", output_docx);

        inputXml.delete();

        return output_docx;
    }
}
