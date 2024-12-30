package com.jaboumal.dto;

import java.time.LocalDate;

/**
 * Competitor object for the XML response.
 * This class is used to represent a competitor in the XML response.
 * It contains the competitor's license number, first name, last name, date of birth and isGuest
 * of the competitor.
 *
 * @author Malo Jaboulet
 */
public class CompetitorDTO {

    private int lizenzNummer;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private boolean isGuest;
    private boolean gewehr;
    private boolean pistole;

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
     * @param isGuest      the competitor's guest status
     */
    public CompetitorDTO(int lizenzNummer, String firstName, String lastName, LocalDate dateOfBirth, boolean isGuest) {
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.isGuest = isGuest;
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
     * Returns if the competitor is a guest.
     *
     * @return if the competitor is a guest
     */
    public boolean isGuest() {
        return isGuest;
    }

    /**
     * Sets if the competitor is a guest.
     *
     * @param guest if the competitor is a guest
     */
    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    /**
     * Returns if the competitor is a gewehr shooter.
     *
     * @return if the competitor is a gewehr shooter
     */
    public boolean isGewehr() {
        return gewehr;
    }

    /**
     * Sets if the competitor is a gewehr shooter.
     *
     * @param gewehr if the competitor is a gewehr shooter
     */
    public void setGewehr(boolean gewehr) {
        this.gewehr = gewehr;
    }

    /**
     * Returns if the competitor is a pistole shooter.
     *
     * @return if the competitor is a pistole shooter
     */
    public boolean isPistole() {
        return pistole;
    }

    /**
     * Sets if the competitor is a pistole shooter.
     *
     * @param pistole if the competitor is a pistole shooter
     */
    public void setPistole(boolean pistole) {
        this.pistole = pistole;
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
