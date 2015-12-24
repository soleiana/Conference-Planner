package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailability;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.core.services.integration.helpers.ConferenceRoomServiceIntegrationTestHelper;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.Assert.*;


public class ConferenceRoomServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @Autowired
    private ConferenceRoomServiceIntegrationTestHelper testHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        testHelper.setNow(LocalDateTime.now());
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        conferenceRoomService.createConferenceRoom(room);
        assertNotNull(room.getId());
    }

    @Test
    public void test_createConferenceRoom_throws_ApplicationException_if_conferenceRoom_exists() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom();
        databaseConfigurator.configureConferenceRoom(room1);
        assertNotNull(room1);
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom();
        expectedException.expect(ApplicationException.class);
        conferenceRoomService.createConferenceRoom(room2);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_no_conference_is_registered() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = new ArrayList<>();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        testHelper.assertGetAvailableConferenceRoomsResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_only_cancelled_conferences_are_registered() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        testHelper.assertGetAvailableConferenceRoomsResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_planned_conference_does_not_overlap_with_scheduled_conferences() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createNonOverlappingConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        testHelper.assertGetAvailableConferenceRoomsResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_throws_ApplicationException_if_no_available_conferenceRoom_exist() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createMixedConferences();
        databaseConfigurator.configure(rooms, conferences);
        expectedException.expect(ApplicationException.class);
        conferenceRoomService.getAvailableConferenceRooms(plannedConference);
    }

    @Test
    public void test_checkIfConferenceRoomsAvailable_is_true_if_all_checked_rooms_are_available() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createNonOverlappingConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conferences);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        boolean result = conferenceRoomService.checkIfConferenceRoomsAvailable(roomIds, plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_upcoming_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(room, conferences);
        ConferenceRoomAvailability availability = conferenceRoomService.getConferenceRoomAvailabilityItems(room.getId());
        assertEquals(conferences.size(), availability.getAvailabilityItems().size());
        testHelper.assertGetConferenceRoomAvailabilityItemsResult(availability.getAvailabilityItems());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_one_cancelled_conference_and_upcoming_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        databaseConfigurator.configureWithConferenceRoomAvailability(room,conferences);
        ConferenceRoomAvailability availability = conferenceRoomService.getConferenceRoomAvailabilityItems(room.getId());
        assertEquals(conferences.size()-1, availability.getAvailabilityItems().size());
        assertNotNull(availability.getConferenceRoom());
        testHelper.assertGetConferenceRoomAvailabilityItemsResult(availability.getAvailabilityItems());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_one_conference_in_past_and_upcoming_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        databaseConfigurator.configureWithConferenceRoomAvailability(room, conferences);
        ConferenceRoomAvailability availability = conferenceRoomService.getConferenceRoomAvailabilityItems(room.getId());
        assertEquals(conferences.size()-1, availability.getAvailabilityItems().size());
        assertNotNull(availability.getConferenceRoom());
        testHelper.assertGetConferenceRoomAvailabilityItemsResult(availability.getAvailabilityItems());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_throws_ApplicationException_if_conferenceRoom_does_not_exist() {
        expectedException.expect(ApplicationException.class);
        conferenceRoomService.getConferenceRoomAvailabilityItems(1);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_throws_ApplicationException_if_no_upcoming_conference_is_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(room, conferences);
        expectedException.expect(ApplicationException.class);
        conferenceRoomService.getConferenceRoomAvailabilityItems(room.getId());
    }

}
