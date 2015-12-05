package com.conferenceplanner.core.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "PARTICIPANT")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Column(name = "BIRTH_DATE", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate birthDate;

    @Column(name = "PASSPORT_NR", nullable = false)
    private String passportNr;

    @ManyToMany(mappedBy = "participants")
    private List<Conference> conferences = new ArrayList<>();

    public Participant() {}

    public Participant(String name, String surname, LocalDate birthDate, String passportNr) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.passportNr = passportNr;
    }

    public Integer getId() {
        return id;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportNr() {
        return passportNr;
    }

    public void setPassportNr(String passportNr) {
        this.passportNr = passportNr;
    }

    public List<Conference> getConferences() {
        return conferences;
    }

    public void setConferences(List<Conference> conferences) {
        this.conferences = conferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (!name.equals(that.name)) return false;
        if (!surname.equals(that.surname)) return false;
        if (!birthDate.equals(that.birthDate)) return false;
        return passportNr.equals(that.passportNr);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + birthDate.hashCode();
        result = 31 * result + passportNr.hashCode();
        return result;
    }
}
