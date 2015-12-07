package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomAvailability {

    private ConferenceRoom conferenceRoom;
    private List<ConferenceRoomAvailabilityEntry> conferenceRoomAvailabilityEntries = new ArrayList<>();

    public ConferenceRoomAvailability() {}

    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public List<ConferenceRoomAvailabilityEntry> getConferenceRoomAvailabilityEntries() {
        return conferenceRoomAvailabilityEntries;
    }

    public void setConferenceRoomAvailabilityEntries(List<ConferenceRoomAvailabilityEntry> conferenceRoomAvailabilityEntries) {
        this.conferenceRoomAvailabilityEntries = conferenceRoomAvailabilityEntries;
    }
}
