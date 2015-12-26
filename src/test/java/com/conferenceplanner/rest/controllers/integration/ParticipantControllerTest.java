package com.conferenceplanner.rest.controllers.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.rest.controllers.ParticipantController;
import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.fixtures.ParticipantFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class ParticipantControllerTest extends SpringContextTest {

    @Autowired
    private ParticipantController controller;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private DatabaseCleaner databaseCleaner;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_addParticipant_returns_CREATED() {
        com.conferenceplanner.core.domain.Conference coreDomainConference =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        Participant participant = ParticipantFixture.createParticipant(coreDomainConference.getId());
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals("Participant added.", response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_BAD_REQUEST() {
        Participant participant = ParticipantFixture.createParticipant(null);
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_NOT_FOUND_if_conference_does_not_exist() {
        Participant participant = ParticipantFixture.createParticipant(1);
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals("No conference found for selected id!", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_NOT_FOUND_if_no_available_conference_exist() {
        com.conferenceplanner.core.domain.Conference coreDomainConference = com.conferenceplanner.core.fixtures.ConferenceFixture.createOngoingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        Participant participant = ParticipantFixture.createParticipant(coreDomainConference.getId());
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals("No available conferences!", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_CONFLICT_if_participant_is_registered_for_conference() {
        com.conferenceplanner.core.domain.Conference coreDomainConference = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        Participant participant1 = ParticipantFixture.createParticipant(coreDomainConference.getId());
        Participant participant2 = ParticipantFixture.createParticipant(coreDomainConference.getId());
        controller.addParticipant(participant1);
        ResponseEntity<String> response = controller.addParticipant(participant2);
        assertEquals("Participant already registered for conference!", response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_OK() {
        com.conferenceplanner.core.domain.Conference coreDomainConference = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        com.conferenceplanner.core.domain.Participant coreDomainParticipant = com.conferenceplanner.core.fixtures.ParticipantFixture.createParticipant();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainConference, coreDomainParticipant);
        Participant participant = ParticipantFixture.createParticipant(coreDomainConference.getId(), coreDomainParticipant.getId());
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals("Participant removed.", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_BAD_REQUEST() {
        Participant participant = ParticipantFixture.createParticipant(1, null);
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_NOT_FOUND_if_conference_does_not_exist() {
        Participant participant = ParticipantFixture.createParticipant(1, 1);
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals("No conference found for selected id!", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_NOT_FOUND_if_no_participant_is_registered_for_conference() {
        com.conferenceplanner.core.domain.Conference coreDomainConference = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        com.conferenceplanner.core.domain.Participant coreDomainParticipant = com.conferenceplanner.core.fixtures.ParticipantFixture.createParticipant();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        databaseConfigurator.configureParticipant(coreDomainParticipant);
        Participant participant = ParticipantFixture.createParticipant(coreDomainConference.getId(), coreDomainParticipant.getId());
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals("No participants found!", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_NOT_FOUND_if_participant_is_not_registered_for_conference() {
        com.conferenceplanner.core.domain.Conference coreDomainConference = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        com.conferenceplanner.core.domain.Participant coreDomainParticipant1 = com.conferenceplanner.core.fixtures.ParticipantFixture.createParticipant("Anna");
        com.conferenceplanner.core.domain.Participant coreDomainParticipant2 = com.conferenceplanner.core.fixtures.ParticipantFixture.createParticipant("Victory");
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainConference, coreDomainParticipant1);
        databaseConfigurator.configureParticipant(coreDomainParticipant2);
        Participant participant = ParticipantFixture.createParticipant(coreDomainConference.getId(), coreDomainParticipant2.getId());
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals("No participant with selected id is registered for conference!", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
