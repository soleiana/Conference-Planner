package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRooms {

    private String errorMessage;
    private String conferenceStartDateTime;
    private String conferenceEndDateTime;
    List<ConferenceRoom> conferenceRooms = new ArrayList<>();

    public ConferenceRooms() {
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

    public List<ConferenceRoom> getConferenceRooms() {
        return conferenceRooms;
    }

    public void setConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        this.conferenceRooms = conferenceRooms;
    }
}
