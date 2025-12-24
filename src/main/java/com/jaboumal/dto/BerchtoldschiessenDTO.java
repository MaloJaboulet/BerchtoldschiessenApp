package com.jaboumal.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for Berchtoldschiessen participants
 *
 * @param firstName    First name of the participant
 * @param lastName     Last name of the participant
 * @param geburtsdatum Birth date of the participant
 * @param barcode      Barcode associated with the participant
 * @param datum        Date of participation (defaults to current date)
 * @param istGast      Indicates if the participant is a guest
 * @param istAktiv     Indicates if the participant is active
 */
public record BerchtoldschiessenDTO(
        String firstName,
        String lastName,
        LocalDate geburtsdatum,
        String barcode,
        LocalDate datum,
        boolean istGast,
        boolean istAktiv) {

    /**
     * Constructor that sets the participation date to the current date
     */
    public BerchtoldschiessenDTO {
        datum = LocalDate.now();
    }
}
