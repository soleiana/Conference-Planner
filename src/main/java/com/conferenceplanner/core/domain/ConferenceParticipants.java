package com.conferenceplanner.core.domain;

import java.util.List;

public class ConferenceParticipants {

    private Conference conference;
    private List<Participant> participants;

    public ConferenceParticipants() {}

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
