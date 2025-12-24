package com.jaboumal.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for Berchtoldschiessen
 * This class is used to map the data from the XML file to a Java object
 *
 * @author Malo Jaboulet
 */
public record BerchtoldschiessenDTO(
        String firstName,
        String lastName,
        LocalDate geburtsdatum,
        String barcode,
        LocalDate datum,
        boolean istGast,
        boolean istAktiv) {

    public BerchtoldschiessenDTO {
        datum = LocalDate.now();
    }
}
