package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.core.services.fixtures.ParticipantFixture;
import com.conferenceplanner.core.services.integration.helpers.ConferenceServiceIntegrationTestHelper;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


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
    public void test_getUpcomingConferences_throws_ApplicationException_if_no_upcoming_conference_exist() {
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configureConferences(conferences);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No upcoming conferences!");
        conferenceService.getUpcomingConferences();
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
    public void test_getAvailableConferences_throws_ApplicationException_if_no_available_conference_exist() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createFullyOccupiedConferenceRooms(rooms.size());
        databaseConfigurator.configure(rooms, conferences, availabilityItems);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No available conferences!");
        conferenceService.getAvailableConferences();
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
    public void test_createConference_throws_ApplicationException_if_conference_exists() {
        Conference conference1 = ConferenceFixture.createUpcomingConference();
        Conference conference2 = ConferenceFixture.cloneConference(conference1);
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference1);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference already exists!");
        conferenceService.createConference(conference2, roomIds);
    }

    @Test
    public void test_createConference_throws_ApplicationException_if_no_conferenceRoom_exists() {
        Conference conference1 = ConferenceFixture.createUpcomingConference("Devoxx");
        Conference conference2 = ConferenceFixture.createUpcomingConference("JavaOne");
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference1);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No conference rooms found for selected conference interval!");
        conferenceService.createConference(conference2, roomIds);
    }

    @Test
    public void test_createConference_throws_ApplicationException_if_conferenceRoom_is_not_available() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureConferenceRooms(rooms);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        roomIds.add(-1);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference room(s) not available!");
        conferenceService.createConference(conference, roomIds);
    }

    @Test
    public void test_cancelConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        conferenceService.cancelConference(conference.getId());
        assertTrue(conference.isCancelled());
    }

    @Test
    public void test_cancelConference_throws_ApplicationException_if_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference already cancelled");
        conferenceService.cancelConference(conference.getId());
    }

    @Test
    public void test_getParticipants() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<Participant> participants = ParticipantFixture.createParticipants(3);
        databaseConfigurator.configure(conference, participants);
        ConferenceParticipants conferenceParticipants = conferenceService.getParticipants(conference.getId());
        assertEquals(participants.size(), conferenceParticipants.getParticipants().size());
        assertEquals(conference, conferenceParticipants.getConference());
    }

    @Test
    public void test_getParticipants_throws_ApplicationException_if_conference_is_not_upcoming() {
        Conference conference = ConferenceFixture.createOngoingConference();
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference is not upcoming!");
        conferenceService.getParticipants(conference.getId());
    }

    @Test
    public void test_getParticipants_throws_ApplicationException_if_no_participant_is_registered() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No participants found!");
        conferenceService.getParticipants(conference.getId());
    }

    @Test
    public void test_checkIfConferenceIsAvailable_is_true_if_conference_is_available() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conferences);
        boolean result = conferenceService.checkIfConferenceIsAvailable(conferences.get(0));
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceIsAvailable_is_false_if_conference_is_not_available() {
        Conference conferenceToCheck = ConferenceFixture.createOngoingConference();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conferences);
        boolean result = conferenceService.checkIfConferenceIsAvailable(conferenceToCheck);
        assertFalse(result);
    }

}
