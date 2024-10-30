package com.jaboumal.dto.xml;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * Data Transfer Object for Berchtoldschiessen
 * This class is used to map the data from the XML file to a Java object
 *
 * @author Malo Jaboulet
 */
public class BerchtoldschiessenDTO {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "geburtsdatum")
    private String geburtsdatum;
    @XmlElement(name = "barcode")
    private String barcode;

    /**
     * Default constructor
     */
    public BerchtoldschiessenDTO() {
    }

    /**
     * Constructor with parameters
     *
     * @param barcode     the barcode of the shooter
     * @param dateOfBirth the date of birth of the shooter
     * @param name        the name of the shooter
     */
    public BerchtoldschiessenDTO(String barcode, String dateOfBirth, String name) {
        this.barcode = barcode;
        this.geburtsdatum = dateOfBirth;
        this.name = name;
    }
}
