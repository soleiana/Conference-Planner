package com.conferenceplanner.rest.controllers.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.fixtures.ParticipantFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.CommonTestHelper;
import com.conferenceplanner.rest.controllers.ConferenceController;
import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceParticipants;
import com.conferenceplanner.rest.domain.Conferences;
import com.conferenceplanner.rest.fixtures.ConferenceFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;


public class ConferenceControllerTest extends SpringContextTest {

    @Autowired
    private ConferenceController controller;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CommonTestHelper commonTestHelper;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_createConference_returns_CREATED() {
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureConferenceRooms(rooms);
        List<Integer> roomIds = commonTestHelper.getConferenceRoomIds(rooms);
        Conference conference = ConferenceFixture.createConference(roomIds);
        ResponseEntity<String> response = controller.createConference(conference);
        assertEquals("Conference created.", response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_createConference_returns_BAD_REQUEST() {
        Conference conference = ConferenceFixture.createConference(null);
        ResponseEntity<String> response = controller.createConference(conference);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_createConference_returns_CONFLICT() {
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureConferenceRooms(rooms);
        List<Integer> roomIds = commonTestHelper.getConferenceRoomIds(rooms);
        Conference conference1 = ConferenceFixture.createConference(roomIds);
        Conference conference2 = ConferenceFixture.createConference(roomIds);
        controller.createConference(conference1);
        ResponseEntity<String> response = controller.createConference(conference2);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_getUpcomingConferences_returns_OK() {
        List<com.conferenceplanner.core.domain.Conference> coreDomainConferences =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConferences();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConferences);
        ResponseEntity<Conferences> response = controller.getUpcomingConferences();
        assertEquals(coreDomainConferences.size(), response.getBody().getConferences().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getUpcomingConferences_returns_NOT_FOUND() {
        List<com.conferenceplanner.core.domain.Conference> coreDomainConferences =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createCancelledConferences();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConferences);
        ResponseEntity<Conferences> response = controller.getUpcomingConferences();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferences_returns_OK() {
        List<com.conferenceplanner.core.domain.Conference> coreDomainConferences =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConferences();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConferences);
        ResponseEntity<Conferences> response = controller.getAvailableConferences();
        assertEquals(coreDomainConferences.size(), response.getBody().getConferences().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferences_returns_NOT_FOUND() {
        List<com.conferenceplanner.core.domain.Conference> coreDomainConferences =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createCancelledConferences();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConferences);
        ResponseEntity<Conferences> response = controller.getAvailableConferences();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getParticipants_returns_OK() {
        com.conferenceplanner.core.domain.Conference coreDomainConference =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        List<Participant> coreDomainParticipants = ParticipantFixture.createParticipants(2);
        databaseConfigurator.configure(coreDomainConference, coreDomainParticipants);
        ResponseEntity<ConferenceParticipants> response = controller.getParticipants(coreDomainConference.getId());
        assertEquals(coreDomainParticipants.size(), response.getBody().getParticipants().size());
        assertNotNull(response.getBody().getConference());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getParticipants_returns_NOT_FOUND() {
        com.conferenceplanner.core.domain.Conference coreDomainConference =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(coreDomainConference);
        ResponseEntity<ConferenceParticipants> response = controller.getParticipants(coreDomainConference.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getParticipants_returns_CONFLICT() {
        com.conferenceplanner.core.domain.Conference coreDomainConference =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createOngoingConference();
        List<Participant> coreDomainParticipants = ParticipantFixture.createParticipants(2);
        databaseConfigurator.configure(coreDomainConference, coreDomainParticipants);
        ResponseEntity<ConferenceParticipants> response = controller.getParticipants(coreDomainConference.getId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_cancelConference_returns_OK() {
        com.conferenceplanner.core.domain.Conference coreDomainConference =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(coreDomainConference);
        ResponseEntity<String> response = controller.cancelConference(coreDomainConference.getId());
        assertEquals("Conference cancelled.", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_cancelConference_returns_NOT_FOUND() {
        ResponseEntity<String> response = controller.cancelConference(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_cancelConference_returns_CONFLICT() {
        com.conferenceplanner.core.domain.Conference coreDomainConference =
                com.conferenceplanner.core.fixtures.ConferenceFixture.createCancelledConference();
        databaseConfigurator.configureConference(coreDomainConference);
        ResponseEntity<String> response = controller.cancelConference(coreDomainConference.getId());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

}
