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

/**
 * Service class for creating and loading XML files
 *
 * @author Malo Jaboulet
 */
public class XMLService {
    private static final Logger log = LoggerFactory.getLogger(XMLService.class);

    /**
     * Create an XML file with the given name, date of birth and barcode
     *
     * @param name        the name of the competitor
     * @param dateOfBirth the date of birth of the competitor
     * @param barcode     the barcode of the competitor
     * @throws JAXBException if an error occurs during the creation of the XML file
     */
    public void createXml(String name, String dateOfBirth, String barcode) throws JAXBException {
        BerchtoldschiessenDTO berchtoldschiessenDTO = new BerchtoldschiessenDTO(barcode, dateOfBirth, name);
        RootDTO rootDTO = new RootDTO(berchtoldschiessenDTO);

        // create XML file
        File file = new File(FilePaths.getPath(INPUT_XML_PATH));
        JAXBContext jaxbContext = JAXBContext.newInstance(RootDTO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // write data to file
        jaxbMarshaller.marshal(rootDTO, file);
    }

    /**
     * Load the XML data in the docx file
     *
     * @param competitorName the name of the competitor
     * @return the path of the output docx file
     * @throws FileNotFoundException if the input XML file is not found
     * @throws Docx4JException       if an error occurs during the loading of the XML data in the docx file
     */
    public String loadXMLDataInDocxFile(String competitorName) throws FileNotFoundException, Docx4JException {
        String outputDocxPath = String.format(FilePaths.getPath(OUTPUT_DOCX_PATH), competitorName);

        File inputXml = new File(FilePaths.getPath(INPUT_XML_PATH));
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(FilePaths.getPath(INPUT_DOCX_PATH)));
        FileInputStream xmlStream = new FileInputStream(inputXml);
        // Insert the XML data into the docx
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT);
        Docx4J.save(wordMLPackage, new File(outputDocxPath), Docx4J.FLAG_NONE);
        log.info("Saved: {}", outputDocxPath);

        inputXml.delete();

        return outputDocxPath;
    }
}
