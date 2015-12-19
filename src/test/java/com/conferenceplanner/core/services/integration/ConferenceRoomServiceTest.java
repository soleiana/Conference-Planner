package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.core.services.integration.helpers.ConferenceRoomServiceTestHelper;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;
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
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @Autowired
    private ConferenceRoomServiceTestHelper conferenceRoomServiceTestHelper;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conferenceRoomServiceTestHelper.setNow(LocalDateTime.now());
    }

    @Test
    public void test_checkIfExists_is_true_if_conference_room_exists() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(7);
        conferenceRoomRepository.create(room1);
        assertNotNull(room1.getId());
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(6);
        boolean result = conferenceRoomService.checkIfConferenceRoomExists(room2);
        assertTrue(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_conference_room_does_not_exist() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(7);
        conferenceRoomRepository.create(room1);
        assertNotNull(room1.getId());
        ConferenceRoom room2 = ConferenceRoomFixture.createAnotherConferenceRoom(7);
        boolean result = conferenceRoomService.checkIfConferenceRoomExists(room2);
        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_no_conference_room_exists() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        boolean result = conferenceRoomService.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_create() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        conferenceRoomService.createConferenceRoom(room);
        assertNotNull(room.getId());
    }

    @Test
    public void test_getAvailableConferenceRooms_if_no_conference_is_registered() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = new ArrayList<>();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        conferenceRoomServiceTestHelper.assertGetAvailableConferenceRoomsResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_planned_conference_overlaps_with_scheduled_conference() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createMixedConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertTrue(availableRooms.isEmpty());
    }

    @Test
    public void test_getAvailableConferenceRooms_if_only_cancelled_conferences_are_registered() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        conferenceRoomServiceTestHelper.assertGetAvailableConferenceRoomsResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_planned_conference_does_not_overlap_with_scheduled_conferences() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createNonOverlappingConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        conferenceRoomServiceTestHelper.assertGetAvailableConferenceRoomsResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_upcoming_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size(), availabilityItems.size());
        conferenceRoomServiceTestHelper.assertGetConferenceRoomAvailabilityItemsResult(availabilityItems);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_only_cancelled_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertTrue(availabilityItems.isEmpty());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_one_cancelled_conference_and_upcoming_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size()-1, availabilityItems.size());
        conferenceRoomServiceTestHelper.assertGetConferenceRoomAvailabilityItemsResult(availabilityItems);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_one_conference_in_past_and_upcoming_conferences_are_registered() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size()-1, availabilityItems.size());
        conferenceRoomServiceTestHelper.assertGetConferenceRoomAvailabilityItemsResult(availabilityItems);
    }

}