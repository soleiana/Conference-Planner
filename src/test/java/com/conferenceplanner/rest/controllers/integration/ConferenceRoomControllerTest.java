package com.conferenceplanner.rest.controllers.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.rest.controllers.ConferenceRoomController;
import com.conferenceplanner.rest.domain.ConferenceRooms;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.ConferenceRoomAvailability;
import com.conferenceplanner.rest.fixtures.ConferenceFixture;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;


public class ConferenceRoomControllerTest extends SpringContextTest {

    @Autowired
    private ConferenceRoomController controller;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private DatabaseCleaner databaseCleaner;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_createConferenceRoom_returns_CREATED() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        ResponseEntity<String> response = controller.createConferenceRoom(room);
        assertEquals("Conference room created.", response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_createConferenceRoom_returns_BAD_REQUEST() {
        ConferenceRoom room = ConferenceRoomFixture.createInvalidConferenceRoom();
        ResponseEntity<String> response = controller.createConferenceRoom(room);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_createConferenceRoom_returns_CONFLICT() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        controller.createConferenceRoom(room);
        ResponseEntity<String> response = controller.createConferenceRoom(room);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferenceRooms_returns_OK() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom();
        ConferenceRoom room2 = ConferenceRoomFixture.createAnotherConferenceRoom();
        controller.createConferenceRoom(room1);
        controller.createConferenceRoom(room2);
        String conferenceStartDateTime = ConferenceFixture.getStartDateTime();
        String conferenceEndDateTime = ConferenceFixture.getEndDateTime();
        ResponseEntity<ConferenceRooms> response = controller.getAvailableConferenceRooms(conferenceStartDateTime, conferenceEndDateTime);
        assertEquals(2, response.getBody().getConferenceRooms().size());
        assertEquals(conferenceStartDateTime, response.getBody().getConferenceStartDateTime());
        assertEquals(conferenceEndDateTime, response.getBody().getConferenceEndDateTime());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferenceRooms_returns_BAD_REQUEST() {
        String conferenceEndDateTime = ConferenceFixture.getEndDateTime();
        ResponseEntity<ConferenceRooms> response = controller.getAvailableConferenceRooms(null, conferenceEndDateTime);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferenceRooms_returns_NOT_FOUND() {
        String conferenceStartDateTime = ConferenceFixture.getStartDateTime();
        String conferenceEndDateTime = ConferenceFixture.getEndDateTime();
        ResponseEntity<ConferenceRooms> response = controller.getAvailableConferenceRooms(conferenceStartDateTime, conferenceEndDateTime);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getConferenceRoomAvailability_returns_OK() {
        com.conferenceplanner.core.domain.ConferenceRoom coreDomainRoom =
                com.conferenceplanner.core.repositories.fixtures.ConferenceRoomFixture.createConferenceRoom("name");
        List<Conference> coreDomainConferences = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConferences);
        ResponseEntity<ConferenceRoomAvailability> response = controller.getConferenceRoomAvailability(coreDomainRoom.getId());
        assertEquals(coreDomainConferences.size(), response.getBody().getConferenceRoomAvailabilityItems().size());
        assertNotNull(response.getBody().getConferenceRoom());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getConferenceRoomAvailability_returns_BAD_REQUEST() {
        ResponseEntity<ConferenceRoomAvailability> response = controller.getConferenceRoomAvailability(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_getConferenceRoomAvailability_returns_NOT_FOUND() {
        com.conferenceplanner.core.domain.ConferenceRoom coreDomainRoom1 =
                com.conferenceplanner.core.repositories.fixtures.ConferenceRoomFixture.createConferenceRoom("name1");
        com.conferenceplanner.core.domain.ConferenceRoom coreDomainRoom2 =
                com.conferenceplanner.core.repositories.fixtures.ConferenceRoomFixture.createConferenceRoom("name2");
        List<Conference> coreDomainConferences = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom1, coreDomainConferences);
        databaseConfigurator.configureConferenceRoom(coreDomainRoom2);
        ResponseEntity<ConferenceRoomAvailability> response = controller.getConferenceRoomAvailability(coreDomainRoom2.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
