package com.conferenceplanner.core.services.fixtures;

import com.conferenceplanner.core.domain.ConferenceRoom;

public class ConferenceRoomFixture {

    private static final String NAME = "Devoxx";
    private static final String LOCATION = "Antwerpen";
    private static final int MAX_SEATS = 2000;

    public static ConferenceRoom createConferenceRoom() {
        return new ConferenceRoom(NAME, LOCATION, MAX_SEATS);
    }
}
