package com.conferenceplanner.core.services.fixtures;

import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomAvailabilityItemFixture {

    public static List<ConferenceRoomAvailabilityItem> createConferenceRoomsWithAvailableSeats(){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(i*100));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createFullyOccupiedConferenceRooms(){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(0));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createPartiallyOccupiedConferenceRooms(){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            if (i == 1) {
                availabilityItems.add(new ConferenceRoomAvailabilityItem(0));
            }
            availabilityItems.add(new ConferenceRoomAvailabilityItem(i*100));
        }
        return availabilityItems;
    }
}
