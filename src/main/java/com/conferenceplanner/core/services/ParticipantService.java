package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class ParticipantService {

    @Autowired
    private ParticipantServiceAssistant serviceAssistant;

    @Autowired
    private ConferenceService conferenceService;


    public void removeParticipant(int participantId, int conferenceId) {
        Conference conference = conferenceService.getConference(conferenceId);
        if (!conference.isUpcoming()) {
            throw new ApplicationException("Conference is not upcoming!", ApplicationErrorCode.CONFLICT);
        }
        Participant participant = serviceAssistant.getParticipant(participantId);
        List<Participant> registeredParticipants = conferenceService.getParticipants(conference);

        if (registeredParticipants.contains(participant)) {
            throw new ApplicationException("No participant with selected id registered in conference!", ApplicationErrorCode.NOT_FOUND);
        }
        serviceAssistant.removeParticipant(participant, conference);
    }

}
