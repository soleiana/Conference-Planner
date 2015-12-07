package com.conferenceplanner.rest.domain;

public class ConferenceRoomAvailabilityEntry {

    private Conference conference;
    private int availableSeats;

    public ConferenceRoomAvailabilityEntry() {}

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
