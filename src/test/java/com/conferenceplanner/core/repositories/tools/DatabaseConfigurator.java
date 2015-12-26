package com.conferenceplanner.core.repositories.tools;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class DatabaseConfigurator {

    @Autowired
    private DatabaseController databaseController;


    public void configureConferences(List<Conference> conferences) {
        databaseController.persistConferences(conferences);
    }

    public void configureConference(Conference conference) {
        databaseController.persistConference(conference);
    }

    public void configureConferenceRoom(ConferenceRoom conferenceRoom) {
        databaseController.persistConferenceRoom(conferenceRoom);
    }

    public void configureConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        databaseController.persistConferenceRooms(conferenceRooms);
    }

    public void configureParticipant(Participant participant) {
        databaseController.persistParticipant(participant);
    }

    public void configure(Conference conference, List<Participant> participants) {
        databaseController.persistConference(conference);
        databaseController.persistParticipants(participants);
        databaseController.setupRelationship(conference, participants);
    }

    public void configure(Conference conference, Participant participant) {
        databaseController.persistConference(conference);
        databaseController.persistParticipant(participant);
        databaseController.setupRelationship(conference, participant);
    }

    public void configureWithConferenceRoomAvailability(Conference conference, Participant participant) {
        databaseController.persistConference(conference);
        databaseController.persistParticipant(participant);
        databaseController.setupRelationshipAndTakeAvailableSeat(conference, participant);
    }

    public void configure(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        databaseController.persistConferences(conferences);
        databaseController.persistConferenceRooms(conferenceRooms);
        databaseController.setupRelationship(conferenceRooms, conferences);
    }

    public void configure(List<ConferenceRoom> conferenceRooms, List<Conference> conferences, List<ConferenceRoomAvailabilityItem> availabilityItems) {
        databaseController.persistConferences(conferences);
        databaseController.persistConferenceRooms(conferenceRooms);
        databaseController.setupRelationship(conferenceRooms, conferences, availabilityItems);
    }

    public void configureWithConferenceRoomAvailability(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        databaseController.persistConferences(conferences);
        databaseController.persistConferenceRooms(conferenceRooms);
        databaseController.setupRelationshipWithAvailability(conferenceRooms, conferences);
    }

    public void configureWithConferenceRoomAvailability(ConferenceRoom conferenceRoom, List<Conference> conferences) {
        databaseController.persistConferences(conferences);
        databaseController.persistConferenceRoom(conferenceRoom);
        databaseController.setupRelationshipWithAvailability(conferenceRoom, conferences);
    }

    public void configureWithConferenceRoomAvailability(List<ConferenceRoom> conferenceRooms, Conference conference) {
        databaseController.persistConference(conference);
        databaseController.persistConferenceRooms(conferenceRooms);
        databaseController.setupRelationshipWithAvailability(conferenceRooms, conference);
    }

    public void configureWithConferenceRoomAvailability(ConferenceRoom conferenceRoom, Conference conference) {
        databaseController.persistConference(conference);
        databaseController.persistConferenceRoom(conferenceRoom);
        databaseController.setupRelationshipWithAvailability(conferenceRoom, conference);
    }
}
