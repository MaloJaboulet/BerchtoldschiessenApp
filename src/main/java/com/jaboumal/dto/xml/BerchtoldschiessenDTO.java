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
    @XmlElement(name = "firstName")
    private String firstName;
    @XmlElement(name = "lastName")
    private String lastName;
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
     * @param firstName    the first name of the competitor
     * @param lastName     the last name of the competitor
     */
    public BerchtoldschiessenDTO(String barcode, LocalDate geburtsdatum, String firstName, String lastName, boolean istAktiv, boolean istGast) {
        this.barcode = barcode;
        this.datum = LocalDate.now().format(dateTimeFormatter);
        this.geburtsdatum = DateUtil.dateToString(geburtsdatum);
        this.istAktiv = istAktiv;
        this.istGast = istGast;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getBarcode() {
        return barcode;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public String getDatum() {
        return datum;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public boolean isIstAktiv() {
        return istAktiv;
    }

    public boolean isIstGast() {
        return istGast;
    }

    public String getLastName() {
        return lastName;
    }
}
