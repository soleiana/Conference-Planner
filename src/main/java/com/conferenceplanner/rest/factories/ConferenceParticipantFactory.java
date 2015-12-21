package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.rest.domain.ConferenceParticipants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ConferenceParticipantFactory {

    @Autowired
    private ParticipantFactory participantFactory;

    @Autowired
    private ConferenceFactory conferenceFactory;

    public ConferenceParticipants create(com.conferenceplanner.core.domain.ConferenceParticipants conferenceParticipants) {
        ConferenceParticipants restDomainConferenceParticipants = new ConferenceParticipants();

        List<com.conferenceplanner.rest.domain.Participant> restDomainParticipants = conferenceParticipants.getParticipants()
                .stream()
                .map(participantFactory::create)
                .collect(Collectors.toList());

        Conference conference = conferenceParticipants.getConference();
        com.conferenceplanner.rest.domain.Conference restDomainConference = conferenceFactory.create(conference);
        restDomainConferenceParticipants.setParticipants(restDomainParticipants);
        restDomainConferenceParticipants.setConference(restDomainConference);
        return restDomainConferenceParticipants;
    }
}
