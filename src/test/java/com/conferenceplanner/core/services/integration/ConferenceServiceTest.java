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
    public void test_createConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureConferenceRooms(rooms);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        conferenceService.createConference(conference, roomIds);
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
