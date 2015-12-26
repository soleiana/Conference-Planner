package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceRoomChecker;
import com.conferenceplanner.core.services.ConferenceRoomServiceAssistant;
import com.conferenceplanner.core.fixtures.ConferenceFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ConferenceRoomServiceAssistantTest {

    @InjectMocks
    private ConferenceRoomServiceAssistant serviceAssistant;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private ConferenceRoomChecker conferenceRoomChecker;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        serviceAssistant.createConferenceRoom(room);
        verify(conferenceRoomRepository, times(1)).create(room);
    }

    @Test
    public void test_getConferenceRoom() {
        int id = 1;
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        when(conferenceRoomRepository.getById(id)).thenReturn(room);
        ConferenceRoom conferenceRoom = serviceAssistant.getConferenceRoom(id);
        assertEquals(room, conferenceRoom);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_no_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoom> emptyList = new ArrayList<>();
        when(conferenceRoomRepository.getAll()).thenReturn(emptyList);
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_true_if_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom(10);
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        when(conferenceRoomChecker.compare(room, rooms.get(0))).thenReturn(false);
        when(conferenceRoomChecker.compare(room, rooms.get(1))).thenReturn(true);
        when(conferenceRoomRepository.getAll()).thenReturn(rooms);
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_conference_room_does_not_exist()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom(10);
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        when(conferenceRoomChecker.compare(room, rooms.get(0))).thenReturn(false);
        when(conferenceRoomChecker.compare(room, rooms.get(1))).thenReturn(false);
        when(conferenceRoomRepository.getAll()).thenReturn(rooms);
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_conference_is_upcoming()  {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        Conference conference = ConferenceFixture.createUpcomingConference();
        conferenceRoom.addConference(conference);
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(conference, 3);
        List<ConferenceRoomAvailabilityItem> actualAvailabilityItems = serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom);
        assertEquals(availabilityItems.size(), actualAvailabilityItems.size());
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_if_conference_is_cancelled()  {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        Conference conference = ConferenceFixture.createCancelledConference();
        conferenceRoom.addConference(conference);
        ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(conference, 3);
        List<ConferenceRoomAvailabilityItem> actualAvailabilityItems = serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom);
        assertTrue(actualAvailabilityItems.isEmpty());
    }

}
