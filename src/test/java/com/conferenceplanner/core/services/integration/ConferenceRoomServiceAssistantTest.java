package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ConferenceRoomServiceAssistant;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConferenceRoomServiceAssistantTest extends SpringContextTest {

    @Autowired
    private ConferenceRoomServiceAssistant serviceAssistant;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_true_if_conference_room_exists() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(7);
        databaseConfigurator.configureConferenceRoom(room1);
        assertNotNull(room1.getId());
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(6);
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room2);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_conference_room_does_not_exist() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(7);
        databaseConfigurator.configureConferenceRoom(room1);
        assertNotNull(room1.getId());
        ConferenceRoom room2 = ConferenceRoomFixture.createAnotherConferenceRoom(7);
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room2);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_no_conference_room_exists() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        serviceAssistant.createConferenceRoom(room);
        assertNotNull(room.getId());
    }

}
