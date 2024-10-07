package com.jaboumal.dto;

import jakarta.xml.bind.annotation.XmlElement;


public class BerchtoldschiessenDTO {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "geburtsdatum")
    private String geburtsdatum;
    @XmlElement(name = "barcode")
    private String barcode;

    public BerchtoldschiessenDTO() {
    }

    public BerchtoldschiessenDTO(String barcode, String dateOfBirth, String name) {
        this.barcode = barcode;
        this.geburtsdatum = dateOfBirth;
        this.name = name;
    }
}
