package com.conferenceplanner.rest.fixtures;

import com.conferenceplanner.rest.domain.ConferenceRoom;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConferenceRoomFixture {

    private static final String NAME = "M/S Baltic Queen conference";
    private static final String ANOTHER_NAME = "Radisson Blu conference";
    private static final String LOCATION = "M/S Baltic Queen";
    private static final String ANOTHER_LOCATION = "Radisson Blu";
    private static final Integer MAX_SEATS = 200;

    public static ConferenceRoom createConferenceRoom() {
        return new ConferenceRoom(NAME, LOCATION, MAX_SEATS);
    }

    public static List<ConferenceRoom> createConferenceRooms(int number) {
        return Stream.iterate(1, i -> i++)
                .limit(number)
                .map(i -> new ConferenceRoom())
                .collect(Collectors.toList());
    }

    public static ConferenceRoom createAnotherConferenceRoom() {
        return new ConferenceRoom(ANOTHER_NAME, ANOTHER_LOCATION, MAX_SEATS);
    }

    public static ConferenceRoom createInvalidConferenceRoom() {
        return new ConferenceRoom(NAME, LOCATION, null);
    }
}
