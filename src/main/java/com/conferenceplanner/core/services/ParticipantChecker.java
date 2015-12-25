package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Participant;
import org.springframework.stereotype.Component;

@Component
public class ParticipantChecker {

    public boolean compare(Participant participant, Participant participantToCompare) {
        return participantToCompare.getName().equalsIgnoreCase(participant.getName())
                && participantToCompare.getSurname().equalsIgnoreCase(participant.getSurname())
                && participantToCompare.getBirthDate().equals(participant.getBirthDate())
                && participantToCompare.getPassportNr().equalsIgnoreCase(participant.getPassportNr());
    }
}
