package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomSchedule {

    private ConferenceRoom conferenceRoom;
    private List<ConferenceRoom> conferences = new ArrayList<>();

    public ConferenceRoomSchedule() {}

    public ConferenceRoom getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(ConferenceRoom conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public List<ConferenceRoom> getConferences() {
        return conferences;
    }

    public void setConferences(List<ConferenceRoom> conferences) {
        this.conferences = conferences;
    }
}
