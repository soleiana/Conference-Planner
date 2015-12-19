package com.conferenceplanner.rest.domain;

import java.util.ArrayList;
import java.util.List;

public class ConferenceParticipants {

    private String errorMessage;
    private Conference conference;
    private List<Participant> participants = new ArrayList<>();

    public ConferenceParticipants() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
