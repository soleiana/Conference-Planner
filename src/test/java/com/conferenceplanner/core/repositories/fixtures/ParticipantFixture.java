package com.conferenceplanner.core.repositories.fixtures;

import com.conferenceplanner.core.domain.Participant;

import java.time.LocalDate;


public class ParticipantFixture {

    private static final String NAME = "Ivo";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1980, 2, 25);
    private static final String PASSPORT_NR = "AB12345";

    public static Participant createParticipant(String surname) {
        return new Participant(NAME, surname, BIRTH_DATE, PASSPORT_NR);
    }
}
