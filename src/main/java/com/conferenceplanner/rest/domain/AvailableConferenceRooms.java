package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class AvailableConferenceRooms {

    private ConferenceInterval conferenceInterval;
    List<ConferenceRoom> availableConferenceRooms = new ArrayList<>();

    public AvailableConferenceRooms() {
    }

    public ConferenceInterval getConferenceInterval() {
        return conferenceInterval;
    }

    public void setConferenceInterval(ConferenceInterval conferenceInterval) {
        this.conferenceInterval = conferenceInterval;
    }

    public List<ConferenceRoom> getAvailableConferenceRooms() {
        return availableConferenceRooms;
    }

    public void setAvailableConferenceRooms(List<ConferenceRoom> availableConferenceRooms) {
        this.availableConferenceRooms = availableConferenceRooms;
    }
}
