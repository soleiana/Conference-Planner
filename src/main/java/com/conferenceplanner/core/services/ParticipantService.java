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

        if (!registeredParticipants.contains(participant)) {
            throw new ApplicationException("No participant with selected id is registered for conference!", ApplicationErrorCode.NOT_FOUND);
        }
        serviceAssistant.removeParticipant(participant, conference);
    }

    public void addParticipant(Participant participant, int conferenceId) {
        Conference conference = conferenceService.getConference(conferenceId);

        if (!conferenceService.checkIfConferenceIsAvailable(conference)) {
            throw new ApplicationException("Conference is not available for registration!", ApplicationErrorCode.CONFLICT);
        }
        Participant registeredParticipant = serviceAssistant.getParticipant(participant);

        if (registeredParticipant != null) {
            if (serviceAssistant.checkIfParticipantIsRegisteredForConference(participant, conference)) {
                throw new ApplicationException("Participant already registered for conference!", ApplicationErrorCode.CONFLICT);
            }
        } else {
            serviceAssistant.createParticipant(participant);
        }
        serviceAssistant.registerParticipant(participant, conference);
    }

}
