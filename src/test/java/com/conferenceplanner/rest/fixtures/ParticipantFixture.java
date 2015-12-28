package com.conferenceplanner.rest.fixtures;

import com.conferenceplanner.rest.domain.Participant;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParticipantFixture {

    private static final String NAME = "Ivo";
    private static final String ANOTHER_NAME = "Maris";
    private static final String SURNAME = "Star";
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String BIRTH_DATE = LocalDate.of(1980, 1, 1).format(BIRTH_DATE_FORMATTER);
    private static final String PASSPORT_NR = "AB12345";

    public static Participant createParticipant(Integer conferenceId) {
        Participant participant =  new Participant(NAME, SURNAME, BIRTH_DATE, PASSPORT_NR);
        participant.setConferenceId(conferenceId);
        return participant;
    }

    public static Participant createAnotherParticipant(Integer conferenceId) {
        Participant participant =  new Participant(ANOTHER_NAME, SURNAME, BIRTH_DATE, PASSPORT_NR);
        participant.setConferenceId(conferenceId);
        return participant;
    }

    public static Participant createParticipant(Integer conferenceId, Integer participantId) {
        Participant participant =  new Participant();
        participant.setConferenceId(conferenceId);
        participant.setId(participantId);
        return participant;
    }

    public static List<Participant> createParticipants(int number) {
        return Stream.iterate(1, i -> i++)
                .limit(number)
                .map(i -> new Participant())
                .collect(Collectors.toList());
    }
}
