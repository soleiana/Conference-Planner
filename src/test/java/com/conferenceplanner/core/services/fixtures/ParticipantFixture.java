package com.conferenceplanner.core.services.fixtures;

import com.conferenceplanner.core.domain.Participant;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParticipantFixture {

    private static final String NAME = "Ivo";
    private static final String SURNAME = "Star";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1980, 2, 25);
    private static final String PASSPORT_NR = "AB12345";

    public static Participant createParticipant() {
        return new Participant(NAME, SURNAME, BIRTH_DATE, PASSPORT_NR);
    }

    public static Participant createParticipant(String name) {
        return new Participant(name, SURNAME, BIRTH_DATE, PASSPORT_NR);
    }

    public static List<Participant> createParticipants(int number) {
        List<Participant> participants = new ArrayList<>();
        for (Integer i = 1; i <= number; i++) {
            String name = "name" + i.toString();
            String surname = "surname" + i.toString();
            String passportNumber = PASSPORT_NR + i.toString();
            LocalDate birthDate = BIRTH_DATE.plusYears(1);
            participants.add(new Participant(name, surname, birthDate, passportNumber));
        }
        return participants;
    }

}
