package com.conferenceplanner.core.services.fixtures;

import com.conferenceplanner.core.domain.ConferenceRoom;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomFixture {

    private static final String NAME_1 = "Devoxx";
    private static final String LOCATION_1 = "Antwerpen";
    private static final String NAME_2 = "JFokus";
    private static final String LOCATION_2 = "Stockholm";
    private static final int MAX_SEATS = 2000;

    public static ConferenceRoom createConferenceRoom() {
        return new ConferenceRoom("name3", "location3", MAX_SEATS);
    }

    public static ConferenceRoom createConferenceRoom(int maxSeats) {
        return new ConferenceRoom(NAME_1, LOCATION_1, maxSeats);
    }

    public static ConferenceRoom createAnotherConferenceRoom(int maxSeats) {
        return new ConferenceRoom(NAME_2, LOCATION_2, maxSeats);
    }

    public static List<ConferenceRoom> createConferenceRooms() {
        List<ConferenceRoom> rooms = new ArrayList<>();
        rooms.add(createConferenceRoom(5));
        rooms.add(createAnotherConferenceRoom(5));
        return rooms;
    }

    public static List<ConferenceRoom> createConferenceRooms(int numberOfRooms) {
        List<ConferenceRoom> rooms = new ArrayList<>();
        for (Integer i = 1; i <= numberOfRooms; i++) {
            rooms.add(new ConferenceRoom("name" + i.toString(), "location", 100));
        }
        return rooms;
    }
}
