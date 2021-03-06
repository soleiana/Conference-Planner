package com.conferenceplanner.core.fixtures;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;

import java.util.ArrayList;
import java.util.List;


public class ConferenceRoomAvailabilityItemFixture {

    public static List<ConferenceRoomAvailabilityItem> createConferenceRoomsWithAvailableSeats(int numberOfRooms){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(i*100));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createConferenceRoomsWithAvailableSeats(int numberOfRooms, int maxSeats){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(maxSeats));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createConferenceRoomsWithAvailableSeats(List<Conference> conferences){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (Conference conference: conferences) {
            List<ConferenceRoomAvailabilityItem> items = createConferenceRoomsWithAvailableSeats(conference, 1);
            availabilityItems.addAll(items);
        }
        return availabilityItems;

    }

    public static List<ConferenceRoomAvailabilityItem> createConferenceRoomsWithAvailableSeats(Conference conference, int numberOfRooms){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            ConferenceRoomAvailabilityItem item = new ConferenceRoomAvailabilityItem(i*100);
            item.setConference(conference);
            availabilityItems.add(item);
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createFullyOccupiedConferenceRooms(int numberOfRooms){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            availabilityItems.add(new ConferenceRoomAvailabilityItem(0));
        }
        return availabilityItems;
    }

    public static List<ConferenceRoomAvailabilityItem> createPartiallyOccupiedConferenceRooms(int numberOfRooms){
        List<ConferenceRoomAvailabilityItem> availabilityItems = new ArrayList<>();
        for (int i = 1; i <= numberOfRooms; i++) {
            if (i == 1) {
                availabilityItems.add(new ConferenceRoomAvailabilityItem(0));
            }
            else {
                availabilityItems.add(new ConferenceRoomAvailabilityItem(100));
            }
        }
        return availabilityItems;
    }
}
