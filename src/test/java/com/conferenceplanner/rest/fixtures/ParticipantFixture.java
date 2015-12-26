package com.conferenceplanner.rest.fixtures;

import com.conferenceplanner.rest.domain.Participant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParticipantFixture {

    public static List<Participant> createParticipants(int number) {
        return Stream.iterate(1, i -> i++)
                .limit(number)
                .map(i -> new Participant())
                .collect(Collectors.toList());
    }
}
