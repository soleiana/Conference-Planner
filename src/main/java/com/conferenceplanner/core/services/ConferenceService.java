package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;


    @Transactional
    public List<Conference> getUpcomingConferences() {
        List<Conference> conferences;

        try {
            conferences = conferenceRepository.getUpcoming();

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return conferences;
    }


}
