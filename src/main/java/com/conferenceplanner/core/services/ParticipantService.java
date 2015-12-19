package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.Participant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ParticipantService {

    @Transactional
    public List<Participant> getParticipants(Conference conference) {
        List<Participant> participants;
        try {
            participants = conference.getParticipants();
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return participants;
    }

}
