package com.conferenceplanner.core.services.component;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ConferenceRoomChecker;
import com.conferenceplanner.core.services.ConferenceRoomService;
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
    private ConferenceRoomChecker conferenceRoomChecker;

    private LocalDateTime now;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        now = LocalDateTime.now();
    }

    @Test
    public void test_checkIfExists_is_true_if_conference_room_exists() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(7);
        conferenceRoomRepository.create(room1);
        assertNotNull(room1.getId());
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(6);
        boolean result = conferenceRoomService.checkIfExists(room2);
        assertTrue(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_conference_room_does_not_exist() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(7);
        conferenceRoomRepository.create(room1);
        assertNotNull(room1.getId());
        ConferenceRoom room2 = ConferenceRoomFixture.createAnotherConferenceRoom(7);
        boolean result = conferenceRoomService.checkIfExists(room2);
        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_no_conference_room_exists() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        boolean result = conferenceRoomService.checkIfExists(room);
        assertFalse(result);
    }

    @Test
    public void test_create() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        conferenceRoomService.create(room);
        assertNotNull(room.getId());
    }

    @Test
    public void test_getAvailableConferenceRooms_if_no_conference() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = new ArrayList<>();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        assertResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_overlapping_conference() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createMixedConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertTrue(availableRooms.isEmpty());
    }

    @Test
    public void test_getAvailableConferenceRooms_if_cancelled_conferences() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        assertResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms_if_nonOverlapping_conferences() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createNonOverlappingConferences();
        databaseConfigurator.configure(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
        assertResult(availableRooms, plannedConference);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_upcoming_conferences() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size(), availabilityItems.size());
        assertResult(availabilityItems);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_cancelled_conferences() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertTrue(availabilityItems.isEmpty());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_cancelled_conference() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size()-1, availabilityItems.size());
        assertResult(availabilityItems);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_conference_in_past() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        databaseConfigurator.configureWithConferenceRoomAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size()-1, availabilityItems.size());
        assertResult(availabilityItems);
    }

    private void assertResult(List<ConferenceRoom> conferenceRooms, Conference plannedConference) {
        for (ConferenceRoom room: conferenceRooms) {
            assertTrue(conferenceRoomChecker.isAvailable(room, plannedConference));
        }
    }

    private void assertResult(List<ConferenceRoomAvailabilityItem> availabilityItems) {
        for (ConferenceRoomAvailabilityItem item: availabilityItems) {
            assertFalse(item.getConference().isCancelled());
            assertTrue(item.getConference().getStartDateTime().isAfter(now));
        }
    }
}
