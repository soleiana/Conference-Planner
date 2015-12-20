package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceChecker conferenceChecker;


    public boolean checkIfConferenceExists(Conference conference) {
        try {
            List<Conference> conferences = conferenceRepository.getUpcoming();

            for (Conference c : conferences) {
                if (c.getName().equalsIgnoreCase(conference.getName())
                        && c.getStartDateTime().equals(conference.getStartDateTime())
                        && c.getEndDateTime().equals(conference.getEndDateTime())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return false;
    }

    public List<Conference> getUpcomingConferences() {
        List<Conference> conferences;
        try {
            conferences = conferenceRepository.getUpcoming();

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return conferences;
    }

    public List<Conference> getAvailableConferences() {
        List<Conference> availableConferences;

        try {
            List<Conference> conferences = conferenceRepository.getUpcoming();
            availableConferences = conferences.stream()
                    .filter(conferenceChecker::isAvailable)
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
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

    public boolean checkIfConferenceIsCancelled(Conference conference) {
      return conference.isCancelled();
    }


    public boolean checkIfConferenceIsUpcoming(Conference conference) {
        return conference.isUpcoming();
    }

    public void createConference(Conference conference) {
        try {
            conferenceRepository.create(conference);

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }


    public void cancelConference(Conference conference) {
        conference.setCancelled(true);
    }

}
