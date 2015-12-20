package com.conferenceplanner.core.domain;

import java.util.List;

public class ConferenceRoomAvailability {

    private ConferenceRoom conferenceRoom;
    private List<ConferenceRoomAvailabilityItem> availabilityItems;

    public ConferenceRoomAvailability() {
    }

    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public List<ConferenceRoomAvailabilityItem> getAvailabilityItems() {
        return availabilityItems;
    }

    public void setAvailabilityItems(List<ConferenceRoomAvailabilityItem> availabilityItems) {
        this.availabilityItems = availabilityItems;
    }
}
