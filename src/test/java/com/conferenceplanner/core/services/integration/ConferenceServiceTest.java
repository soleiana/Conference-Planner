package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.core.services.integration.helpers.ConferenceServiceIntegrationTestHelper;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class ConferenceServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ConferenceServiceIntegrationTestHelper testHelper;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        testHelper.setNow(LocalDateTime.now());
    }

    @Test
    public void test_getUpcomingConferences_if_upcoming_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertEquals(conferences.size(), upcomingConferences.size());
        testHelper.assertGetUpcomingConferencesResult(upcomingConferences);
    }

    @Test
    public void test_getUpcomingConferences_if_only_cancelled_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertTrue(upcomingConferences.isEmpty());
    }

    @Test
    public void test_getUpcomingConferences_if_one_cancelled_conference_and_upcoming_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertEquals(conferences.size()-1, upcomingConferences.size());
        testHelper.assertGetUpcomingConferencesResult(upcomingConferences);
    }

    @Test
    public void test_getUpcomingConferences_if_one_conference_in_past_and_upcoming_conferences_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        databaseConfigurator.configureConferences(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertEquals(conferences.size()-1, upcomingConferences.size());
        testHelper.assertGetUpcomingConferencesResult(upcomingConferences);
    }

    @Test
    public void test_getAvailableConferences_if_no_conference_room_has_available_seats() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);

        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createFullyOccupiedConferenceRooms(rooms.size());
        databaseConfigurator.configure(rooms, conferences, availabilityItems);
        List<Conference> availableConferences = conferenceService.getAvailableConferences();
        assertTrue(availableConferences.isEmpty());
    }

    @Test
    public void test_getAvailableConferences_if_one_conference_room_has_available_seats() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);

        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createPartiallyOccupiedConferenceRooms(rooms.size());
        databaseConfigurator.configure(rooms, conferences, availabilityItems);
        List<Conference> availableConferences = conferenceService.getAvailableConferences();
        assertEquals(conferences.size(), availableConferences.size());
        testHelper.assertGetAvailableConferencesResult(availableConferences);
    }

    @Test
    public void test_getAvailableConferences_if_all_conference_rooms_have_available_seats() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);

        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(rooms.size());
        databaseConfigurator.configure(rooms, conferences, availabilityItems);
        List<Conference> availableConferences = conferenceService.getAvailableConferences();
        assertEquals(conferences.size(), availableConferences.size());
        testHelper.assertGetAvailableConferencesResult(availableConferences);
    }

    @Test
    public void test_getConference_if_conference_does_not_exist() {
        Conference actualConference = conferenceService.getConference(1);
        assertNull(actualConference);
    }

    @Test
    public void test_getConference_if_conference_exists() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        int id = conference.getId();
        Conference actualConference = conferenceService.getConference(id);
        assertEquals(conference, actualConference);
    }

    @Test
    public void test_checkIfConferenceExists_is_true_if_planned_conference_exists() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_no_conferences_exist() {
        Conference plannedConference = ConferenceFixture.createUpcomingConference();
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_name() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        plannedConference.setName(plannedConference.getName()+"1");
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_start_date_time() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        plannedConference.setStartDateTime(plannedConference.getStartDateTime().plusMinutes(1));
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_end_date_time() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        assertNotNull(conference.getId());
        Conference plannedConference = ConferenceFixture.cloneConference(conference);
        plannedConference.setEndDateTime(plannedConference.getEndDateTime().plusMinutes(1));
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceIsCancelled_is_true_if_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        databaseConfigurator.configureConference(conference);
        int id = conference.getId();
        Conference conferenceToCheck = conferenceService.getConference(id);
        boolean result = conferenceService.checkIfConferenceIsCancelled(conferenceToCheck);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceIsCancelled_is_false_if_conference_is_not_cancelled() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        int id = conference.getId();
        Conference conferenceToCheck = conferenceService.getConference(id);
        boolean result = conferenceService.checkIfConferenceIsCancelled(conferenceToCheck);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceIsUpcoming_is_true_if_conference_is_upcoming() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        boolean result = conferenceService.checkIfConferenceIsUpcoming(conference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceIsUpcoming_is_false_if_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        databaseConfigurator.configureConference(conference);
        boolean result = conferenceService.checkIfConferenceIsUpcoming(conference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceIsUpcoming_is_false_if_conference_is_ongoing() {
        Conference conference = ConferenceFixture.createOngoingConference();
        databaseConfigurator.configureConference(conference);
        boolean result = conferenceService.checkIfConferenceIsUpcoming(conference);
        assertFalse(result);
    }

    @Test
    public void test_createConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        conferenceService.createConference(conference);
        assertNotNull(conference.getId());
    }

    @Test
    public void test_cancelConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        conferenceService.cancelConference(conference);
        assertTrue(conference.isCancelled());
    }

}
