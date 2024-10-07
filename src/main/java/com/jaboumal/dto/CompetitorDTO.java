package com.jaboumal.dto;

public class CompetitorDTO {

    private int lizenzNummer;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    public CompetitorDTO() {
    }

    public CompetitorDTO(int lizenzNummer,  String firstName, String lastName, String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lizenzNummer = lizenzNummer;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getLizenzNummer() {
        return lizenzNummer;
    }

    public void setLizenzNummer(int lizenzNummer) {
        this.lizenzNummer = lizenzNummer;
    }
}
