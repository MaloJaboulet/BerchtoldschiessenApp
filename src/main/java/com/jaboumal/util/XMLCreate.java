package com.jaboumal.util;

import com.jaboumal.dto.BerchtoldschiessenDTO;
import com.jaboumal.dto.RootDTO;
import com.jaboumal.constants.FilePaths;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;

import static com.jaboumal.constants.FilePaths.INPUT_XML;

public class XMLCreate {
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
}
