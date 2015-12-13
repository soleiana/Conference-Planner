package com.conferenceplanner.core.repositories.tools;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class DatabaseConfigurator {

    @Autowired
    private DatabaseController databaseController;


    public void configure(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        databaseController.persistConferences(conferences);
        databaseController.persistConferenceRooms(conferenceRooms);
        databaseController.setupRelationship(conferenceRooms, conferences);
    }

    public void configure(List<Conference> conferences) {
        databaseController.persistConferences(conferences);
    }


    public void configureWithConferenceRoomAvailability(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        databaseController.persistConferences(conferences);
        databaseController.persistConferenceRooms(conferenceRooms);
        databaseController.setupRelationshipWithAvailability(conferenceRooms, conferences);
    }
}
