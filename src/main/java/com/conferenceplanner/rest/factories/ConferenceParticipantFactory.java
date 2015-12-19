package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.rest.domain.ConferenceParticipants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ConferenceParticipantFactory {

    @Autowired
    private ParticipantFactory participantFactory;

    @Autowired
    private ConferenceFactory conferenceFactory;

    public ConferenceParticipants create(Conference conference, List<Participant> participants) {
        ConferenceParticipants conferenceParticipants = new ConferenceParticipants();

        List<com.conferenceplanner.rest.domain.Participant> restDomainParticipants = new ArrayList<>();

        for (Participant participant: participants) {
            com.conferenceplanner.rest.domain.Participant restDomainParticipant = participantFactory.create(participant);
            restDomainParticipants.add(restDomainParticipant);
        }
        com.conferenceplanner.rest.domain.Conference restDomainConference = conferenceFactory.create(conference);
        conferenceParticipants.setParticipants(restDomainParticipants);
        conferenceParticipants.setConference(restDomainConference);

        return conferenceParticipants;
    }
}
