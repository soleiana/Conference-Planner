package com.conferenceplanner.core.domain;

public class ConferenceRoomFixture {

    public static final String LOCATION = "Antwerpen";
    public static final int MAX_SEATS = 2000;

    public static ConferenceRoom createConferenceRoom(String name) {
       return new ConferenceRoom(name, LOCATION, MAX_SEATS);
    }
}
