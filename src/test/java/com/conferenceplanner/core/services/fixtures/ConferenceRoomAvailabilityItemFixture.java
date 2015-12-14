package com.conferenceplanner.core.services.fixtures;

import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomAvailabilityItemFixture {

    public static List<ConferenceRoomAvailabilityItem> createConferenceRoomsWithAvailableSeats(int numberOfRooms){
        if (numberOfRooms <= 0) {
            throw new IllegalArgumentException("Number of rooms should be greater than 0");
        }

        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(i*100));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createFullyOccupiedConferenceRooms(int numberOfRooms){
        if (numberOfRooms <= 0) {
            throw new IllegalArgumentException("Number of rooms should be greater than 0");
        }

        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(0));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createPartiallyOccupiedConferenceRooms(int numberOfRooms){
        if (numberOfRooms <= 0) {
            throw new IllegalArgumentException("Number of rooms should be greater than 0");
        }

        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            if (i == 1) {
                availabilityItems.add(new ConferenceRoomAvailabilityItem(0));
            }
            else {
                availabilityItems.add(new ConferenceRoomAvailabilityItem(i*100));
            }
        }
        return availabilityItems;
    }
}
