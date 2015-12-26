package com.conferenceplanner.rest.domain;

public class Participant {

    private Integer id;
    private String name;
    private String surname;
    private String birthDate;
    private String passportNr;
    private Integer conferenceId;

    public Participant() {}

    public Participant(String name, String surname, String birthDate, String passportNr) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.passportNr = passportNr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportNr() {
        return passportNr;
    }

    public void setPassportNr(String passportNr) {
        this.passportNr = passportNr;
    }

    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }
}
