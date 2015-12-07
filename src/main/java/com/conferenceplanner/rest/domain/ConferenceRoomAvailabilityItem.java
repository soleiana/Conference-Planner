package com.conferenceplanner.rest.domain;

public class ConferenceRoomAvailabilityItem {

    private Conference conference;
    private int availableSeats;

    public ConferenceRoomAvailabilityItem() {}

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
