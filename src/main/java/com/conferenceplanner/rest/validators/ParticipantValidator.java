package com.conferenceplanner.rest.validators;

import com.conferenceplanner.rest.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantValidator {

    @Autowired
    private ConferenceValidator conferenceValidator;

    public void validateIds(Participant participant) {
        if (participant.getId() == null) {
            throw new ValidationException("Participant id is null");
        }
        conferenceValidator.validateId(participant.getConferenceId());
    }
}
