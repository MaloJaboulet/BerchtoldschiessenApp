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
        BerchtoldschiessenDTO berchtoldschiessenDTO = new BerchtoldschiessenDTO(barcode, dateOfBirth, name);
        RootDTO rootDTO = new RootDTO(berchtoldschiessenDTO);

        File file = new File(FilePaths.getPath(INPUT_XML_PATH));
        JAXBContext jaxbContext = JAXBContext.newInstance(RootDTO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(rootDTO, file);
    }


    public String loadXMLDataInDocxFile(String competitorName) throws FileNotFoundException, Docx4JException {
        String output_docx = String.format(FilePaths.getPath(OUTPUT_DOCX_PATH), competitorName);

        File inputXml = new File(FilePaths.getPath(INPUT_XML_PATH));
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(FilePaths.getPath(INPUT_DOCX_PATH)));
        FileInputStream xmlStream = new FileInputStream(inputXml);
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT);
        Docx4J.save(wordMLPackage, new File(output_docx), Docx4J.FLAG_NONE);
        log.info("Saved: {}", output_docx);

        inputXml.delete();

        return output_docx;
    }
}
