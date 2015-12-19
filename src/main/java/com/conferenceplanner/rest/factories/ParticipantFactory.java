package com.conferenceplanner.rest.factories;

import com.conferenceplanner.rest.domain.Participant;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ParticipantFactory {

    public Participant create(com.conferenceplanner.core.domain.Participant participant) {
        Participant restDomainParticipant = new Participant();
        restDomainParticipant.setId(participant.getId());
        restDomainParticipant.setName(participant.getName());
        restDomainParticipant.setSurname(participant.getSurname());
        restDomainParticipant.setBirthDate(participant.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        restDomainParticipant.setPassportNr(participant.getPassportNr());
        return restDomainParticipant;
    }
}
