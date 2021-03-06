package com.conferenceplanner.core.repositories.fixtures;

import com.conferenceplanner.core.domain.ConferenceRoom;

public class ConferenceRoomFixture {

    private static final String LOCATION = "Antwerpen";
    private static final int MAX_SEATS = 2000;

    public static ConferenceRoom createConferenceRoom(String name) {
       return new ConferenceRoom(name, LOCATION, MAX_SEATS);
    }
}
