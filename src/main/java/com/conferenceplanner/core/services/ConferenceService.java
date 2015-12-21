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
            throw new AccessException("No upcoming conferences!", AccessErrorCode.NOT_FOUND);
        }
        return conferences;
    }

    public List<Conference> getAvailableConferences() {
        List<Conference> availableConferences = serviceAssistant.getAvailableConferences();
        if (availableConferences.isEmpty()) {
            throw new AccessException("No available conferences!", AccessErrorCode.NOT_FOUND);
        }
        return availableConferences;
    }

    public void createConference(Conference conference, List<Integer> conferenceRoomIds) {

        if (serviceAssistant.checkIfConferenceExists(conference)) {
             throw new AccessException("Conference already exists!", AccessErrorCode.CONFLICT);
        }
        if (!conferenceRoomService.checkIfConferenceRoomsAvailable(conferenceRoomIds, conference)) {
            throw new AccessException("Conference room(s) not available!", AccessErrorCode.CONFLICT);
        }
        serviceAssistant.createConference(conference);
        serviceAssistant.registerConference(conference, conferenceRoomIds);
    }

    public void cancelConference(int conferenceId) {
        Conference conference = serviceAssistant.getConference(conferenceId);
        if (conference == null) {
            throw  new AccessException("No conference found for selected id!", AccessErrorCode.NOT_FOUND);
        }
        if (conference.isCancelled()) {
                throw  new AccessException("Conference already cancelled", AccessErrorCode.CONFLICT);
        }
        conference.setCancelled(true);
    }

    public ConferenceParticipants getParticipants(int conferenceId) {
        ConferenceParticipants conferenceParticipants = new ConferenceParticipants();
        List<Participant> participants;
        Conference conference = serviceAssistant.getConference(conferenceId);
        if (conference == null) {
            throw new AccessException("No conference found for selected id!", AccessErrorCode.NOT_FOUND);
        }
        if (!conference.isUpcoming()) {
            throw new AccessException("Conference is not upcoming!", AccessErrorCode.CONFLICT);
        }
        participants = serviceAssistant.getParticipants(conference);
        if(participants.isEmpty()) {
            throw new AccessException("No participants found!", AccessErrorCode.NOT_FOUND);
        }
        conferenceParticipants.setConference(conference);
        conferenceParticipants.setParticipants(participants);
        return conferenceParticipants;
    }

}
