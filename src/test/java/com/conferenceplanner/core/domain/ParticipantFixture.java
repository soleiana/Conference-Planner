package com.conferenceplanner.core.domain;

import java.time.LocalDate;


public class ParticipantFixture {

    public static final String NAME = "Ivo";
    public static final LocalDate BIRTH_DATE = LocalDate.of(1980, 2, 25);
    public static final String PASSPORT_NR = "AB12345";

    public static Participant createParticipant(String surname) {
        return new Participant(NAME, surname, BIRTH_DATE, PASSPORT_NR);
    }
}
