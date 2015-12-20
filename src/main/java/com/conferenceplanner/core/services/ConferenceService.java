package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceChecker conferenceChecker;

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

    public Conference getConference(int conferenceId) {
        Conference conference;
        try {
            conference = conferenceRepository.getById(conferenceId);
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return conference;
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

    public void cancelConference(Conference conference) {
        try {
            conference.setCancelled(true);
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

}
