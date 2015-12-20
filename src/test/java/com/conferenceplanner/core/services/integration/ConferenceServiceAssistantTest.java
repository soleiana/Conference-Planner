package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ConferenceServiceAssistant;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.integration.helpers.ConferenceServiceIntegrationTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConferenceServiceAssistantTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceServiceAssistant serviceAssistant;

    @Autowired
    private ConferenceServiceIntegrationTestHelper testHelper;

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_checkIfConferenceExists_is_true_if_planned_conference_exists() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_no_conferences_exist() {
        Conference plannedConference = ConferenceFixture.createUpcomingConference();
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_name() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        plannedConference.setName(plannedConference.getName()+"1");
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_start_date_time() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        plannedConference.setStartDateTime(plannedConference.getStartDateTime().plusMinutes(1));
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_end_date_time() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        plannedConference.setEndDateTime(plannedConference.getEndDateTime().plusMinutes(1));
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_createConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        serviceAssistant.createConference(conference);
        assertNotNull(conference.getId());
    }

    @Test
    public void test_registerConference() {
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureConferenceRooms(rooms);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        serviceAssistant.registerConference(conference, roomIds);
        testHelper.assertRegisterConferenceResult(conference, rooms);
    }

}
