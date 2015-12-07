package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomAvailability {

    private ConferenceRoom conferenceRoom;
    private List<ConferenceRoomAvailabilityItem> conferenceRoomAvailabilityItems = new ArrayList<>();

    public ConferenceRoomAvailability() {}

    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems() {
        return conferenceRoomAvailabilityItems;
    }

    public void setConferenceRoomAvailabilityItems(List<ConferenceRoomAvailabilityItem> conferenceRoomAvailabilityItems) {
        this.conferenceRoomAvailabilityItems = conferenceRoomAvailabilityItems;
    }
}
