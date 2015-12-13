package com.conferenceplanner.core.services.component;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class ConferenceRoomServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
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
    public void test_getAvailableConferenceRooms_if_overlapping_conference() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createMixedConferences();
        persistConferenceRooms(rooms);
        persistConferences(conferences);
        setupRelationship(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertTrue(availableRooms.isEmpty());
    }

    @Test
    public void test_getAvailableConferenceRooms_if_cancelled_conferences() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        persistConferenceRooms(rooms);
        persistConferences(conferences);
        setupRelationship(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
    }

    @Test
    public void test_getAvailableConferenceRooms_if_nonOverlapping_conferences() {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        List<Conference> conferences = ConferenceFixture.createNonOverlappingConferences();
        persistConferenceRooms(rooms);
        persistConferences(conferences);
        setupRelationship(rooms, conferences);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_upcoming_conferences() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        persistConferenceRooms(Arrays.asList(room));
        persistConferences(conferences);
        setupRelationshipWithAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size(), availabilityItems.size());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_cancelled_conferences() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        persistConferenceRooms(Arrays.asList(room));
        persistConferences(conferences);
        setupRelationshipWithAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(0, availabilityItems.size());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_cancelled_conference() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        persistConferenceRooms(Arrays.asList(room));
        persistConferences(conferences);
        setupRelationshipWithAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size() - 1, availabilityItems.size());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_conference_in_past() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        persistConferenceRooms(Arrays.asList(room));
        persistConferences(conferences);
        setupRelationshipWithAvailability(Arrays.asList(room),conferences);
        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(room);
        assertEquals(conferences.size() - 1, availabilityItems.size());
    }

    @Transactional
    public void persistConferences(List<Conference> conferences) {
        for (Conference conference: conferences) {
            conferenceRepository.create(conference);
        }
    }

    @Transactional
    public void persistConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        for (ConferenceRoom room: conferenceRooms) {
            conferenceRoomRepository.create(room);
        }
    }

    @Transactional
    public void setupRelationship(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        for(ConferenceRoom room: conferenceRooms) {
            for (Conference conference: conferences)
                setupRelationship(room, conference);
        }
    }

    @Transactional
    public void setupRelationshipWithAvailability(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        for(ConferenceRoom room: conferenceRooms) {
            for (Conference conference: conferences)
                setupRelationshipWithAvailability(room, conference);
        }
    }

    private void setupRelationship(ConferenceRoom conferenceRoom, Conference conference) {
        conferenceRoom.getConferences().add(conference);
    }

    private void setupRelationshipWithAvailability(ConferenceRoom conferenceRoom, Conference conference) {
        ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem = new ConferenceRoomAvailabilityItem(conferenceRoom.getMaxSeats());
        conferenceRoom.getConferenceRoomAvailabilityItems().add(conferenceRoomAvailabilityItem);
        conference.getConferenceRoomAvailabilityItems().add(conferenceRoomAvailabilityItem);
        conferenceRoomAvailabilityItem.setConferenceRoom(conferenceRoom);
        conferenceRoomAvailabilityItem.setConference(conference);

        conferenceRoomAvailabilityItemRepository.create(conferenceRoomAvailabilityItem);
        //conferenceRoom.getConferences().add(conference);
    }

}
