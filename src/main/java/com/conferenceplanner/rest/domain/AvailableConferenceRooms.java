package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class AvailableConferenceRooms {

    private String errorMessage;
    private String conferenceStartDateTime;
    private String conferenceEndDateTime;
    List<ConferenceRoom> availableConferenceRooms = new ArrayList<>();

    public AvailableConferenceRooms() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getConferenceStartDateTime() {
        return conferenceStartDateTime;
    }

    public void setConferenceStartDateTime(String conferenceStartDateTime) {
        this.conferenceStartDateTime = conferenceStartDateTime;
    }

    public String getConferenceEndDateTime() {
        return conferenceEndDateTime;
    }

    public void setConferenceEndDateTime(String conferenceEndDateTime) {
        this.conferenceEndDateTime = conferenceEndDateTime;
    }

    public List<ConferenceRoom> getAvailableConferenceRooms() {
        return availableConferenceRooms;
    }

    public void setAvailableConferenceRooms(List<ConferenceRoom> availableConferenceRooms) {
        this.availableConferenceRooms = availableConferenceRooms;
    }
}
