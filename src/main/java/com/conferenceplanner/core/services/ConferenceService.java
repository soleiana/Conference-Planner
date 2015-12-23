package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceParticipants;
import com.conferenceplanner.core.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Transactional
public class ConferenceService {

    @Autowired
    private ConferenceServiceAssistant serviceAssistant;

    @Autowired
    private ConferenceRoomService conferenceRoomService;


    public List<Conference> getUpcomingConferences() {
        List<Conference> conferences = serviceAssistant.getUpcomingConferences();
        if (conferences.isEmpty()) {
            throw new ApplicationException("No upcoming conferences!", ApplicationErrorCode.NOT_FOUND);
        }
        return conferences;
    }

    public List<Conference> getAvailableConferences() {
        List<Conference> availableConferences = serviceAssistant.getAvailableConferences();
        if (availableConferences.isEmpty()) {
            throw new ApplicationException("No available conferences!", ApplicationErrorCode.NOT_FOUND);
        }
        return availableConferences;
    }

    public void createConference(Conference conference, List<Integer> conferenceRoomIds) {
        if (serviceAssistant.checkIfConferenceExists(conference)) {
             throw new ApplicationException("Conference already exists!", ApplicationErrorCode.CONFLICT);
        }
        if (!conferenceRoomService.checkIfConferenceRoomsAvailable(conferenceRoomIds, conference)) {
            throw new ApplicationException("Conference room(s) not available!", ApplicationErrorCode.CONFLICT);
        }
        serviceAssistant.createConference(conference);
        serviceAssistant.registerConference(conference, conferenceRoomIds);
    }

    public void cancelConference(int conferenceId) {
        Conference conference = getConference(conferenceId);
        if (conference.isCancelled()) {
                throw  new ApplicationException("Conference already cancelled", ApplicationErrorCode.CONFLICT);
        }
        serviceAssistant.cancelConference(conference);
    }

    public ConferenceParticipants getParticipants(int conferenceId) {
        ConferenceParticipants conferenceParticipants = new ConferenceParticipants();
        List<Participant> participants;
        Conference conference = getConference(conferenceId);
        if (!conference.isUpcoming()) {
            throw new ApplicationException("Conference is not upcoming!", ApplicationErrorCode.CONFLICT);
        }
        participants = getParticipants(conference);
        conferenceParticipants.setConference(conference);
        conferenceParticipants.setParticipants(participants);
        return conferenceParticipants;
    }

    public Conference getConference(int conferenceId) {
        Conference conference = serviceAssistant.getConference(conferenceId);
        if (conference == null) {
            throw new ApplicationException("No conference found for selected id!", ApplicationErrorCode.NOT_FOUND);
        }
        return conference;
    }

    public List<Participant> getParticipants(Conference conference) {
        List<Participant> participants = serviceAssistant.getParticipants(conference);
        if(participants.isEmpty()) {
            throw new ApplicationException("No participants found!", ApplicationErrorCode.NOT_FOUND);
        }
        return participants;
    }

}
