package com.jaboumal.dto.xml;

import com.jaboumal.util.DateUtil;
import jakarta.xml.bind.annotation.XmlElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Data Transfer Object for Berchtoldschiessen
 * This class is used to map the data from the XML file to a Java object
 *
 * @author Malo Jaboulet
 */
public class BerchtoldschiessenDTO {
    private final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendPattern("dd.MM.yyyy").toFormatter();
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "geburtsdatum")
    private String geburtsdatum;
    @XmlElement(name = "barcode")
    private String barcode;
    @XmlElement(name = "datum")
    private String datum;
    @XmlElement(name = "istGast")
    private boolean istGast;
    @XmlElement(name = "istAktiv")
    private boolean istAktiv;

    /**
     * Default constructor
     */
    public BerchtoldschiessenDTO() {
    }

    /**
     * Constructor with all attributes
     *
     * @param barcode      the barcode of the competitor
     * @param geburtsdatum the birth date of the competitor
     * @param istAktiv     if the competitor is active
     * @param istGast      if the competitor is a guest
     * @param name         the name of the competitor
     */
    public BerchtoldschiessenDTO(String barcode, LocalDate geburtsdatum, String name, boolean istAktiv, boolean istGast) {
        this.barcode = barcode;
        this.datum = LocalDate.now().format(dateTimeFormatter);
        this.geburtsdatum = DateUtil.dateToString(geburtsdatum);
        this.istAktiv = istAktiv;
        this.istGast = istGast;
        this.name = name;
    }
}
