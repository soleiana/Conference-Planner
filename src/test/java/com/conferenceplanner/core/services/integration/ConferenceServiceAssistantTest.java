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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void test_getUpcomingConferences_if_upcoming_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = serviceAssistant.getUpcomingConferences();
        assertEquals(conferences.size(), upcomingConferences.size());
        testHelper.assertGetUpcomingConferencesResult(upcomingConferences);
    }

    @Test
    public void test_getUpcomingConferences_if_only_cancelled_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = serviceAssistant.getUpcomingConferences();
        assertTrue(upcomingConferences.isEmpty());
    }

    @Test
    public void test_getUpcomingConferences_if_one_cancelled_conference_and_upcoming_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = serviceAssistant.getUpcomingConferences();
        assertEquals(conferences.size()-1, upcomingConferences.size());
        testHelper.assertGetUpcomingConferencesResult(upcomingConferences);
    }

    @Test
    public void test_getUpcomingConferences_if_one_conference_in_past_and_upcoming_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = serviceAssistant.getUpcomingConferences();
        assertEquals(conferences.size()-1, upcomingConferences.size());
        testHelper.assertGetUpcomingConferencesResult(upcomingConferences);
    }

}
