package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomAvailability {

    private String errorMessage;
    private ConferenceRoom conferenceRoom;
    private List<ConferenceRoomAvailabilityItem> conferenceRoomAvailabilityItems = new ArrayList<>();

    public ConferenceRoomAvailability() {}

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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
