package com.jaboumal.dto;

import java.time.LocalDate;

/**
 * Competitor object for the XML response.
 * This class is used to represent a competitor in the XML response.
 * It contains the competitor's license number, first name, last name and date of birth
 * of the competitor.
 *
 * @author Malo Jaboulet
 */
public class CompetitorDTO {

    private int lizenzNummer;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    /**
     * Default constructor.
     */
    public CompetitorDTO() {
    }

    /**
     * Constructor with the competitor's license number, first name, last name and date of birth.
     *
     * @param lizenzNummer the competitor's license number
     * @param firstName    the competitor's first name
     * @param lastName     the competitor's last name
     * @param dateOfBirth  the competitor's date of birth
     */
    public CompetitorDTO(int lizenzNummer, String firstName, String lastName, LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lizenzNummer = lizenzNummer;
    }

    /**
     * Returns the date of birth of the competitor.
     *
     * @return the date of birth of the competitor
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the competitor.
     *
     * @param dateOfBirth the date of birth of the competitor
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns the first name of the competitor.
     *
     * @return the first name of the competitor
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the competitor.
     *
     * @param firstName the first name of the competitor
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the competitor.
     *
     * @return the last name of the competitor
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the competitor.
     *
     * @param lastName the last name of the competitor
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the license number of the competitor.
     *
     * @return the license number of the competitor
     */
    public int getLizenzNummer() {
        return lizenzNummer;
    }

    /**
     * Sets the license number of the competitor.
     *
     * @param lizenzNummer the license number of the competitor
     */
    public void setLizenzNummer(int lizenzNummer) {
        this.lizenzNummer = lizenzNummer;
    }

    /**
     * Returns the string representation of the competitor.
     *
     * @return the string representation of the competitor
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + lizenzNummer + ", " + dateOfBirth;
    }
}
