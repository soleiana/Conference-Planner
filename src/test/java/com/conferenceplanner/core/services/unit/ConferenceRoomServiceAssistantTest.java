package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceRoomAvailabilityItemChecker;
import com.conferenceplanner.core.services.ConferenceRoomChecker;
import com.conferenceplanner.core.services.ConferenceRoomServiceAssistant;
import com.conferenceplanner.core.services.DatabaseException;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
    private ConferenceRoomAvailabilityItemChecker conferenceRoomAvailabilityItemChecker;

    @Mock
    private ConferenceRoomChecker conferenceRoomChecker;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


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
    public void test_createConferenceRoom_throws_DatabaseException() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).create(room);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        serviceAssistant.createConferenceRoom(room);
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
    public void test_getConferenceRoom_throws_DatabaseException() {
        int id = 1;
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getById(id);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        serviceAssistant.getConferenceRoom(id);
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
    public void test_checkIfConferenceRoomExists_throws_DatabaseException()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getAll();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        serviceAssistant.checkIfConferenceRoomExists(room);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems()  {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(3);
        conferenceRoom.setConferenceRoomAvailabilityItems(availabilityItems);
        when(conferenceRoomAvailabilityItemChecker.isActual(availabilityItems.get(0))).thenReturn(false);
        when(conferenceRoomAvailabilityItemChecker.isActual(availabilityItems.get(1))).thenReturn(true);
        when(conferenceRoomAvailabilityItemChecker.isActual(availabilityItems.get(2))).thenReturn(true);

        List<ConferenceRoomAvailabilityItem> actualAvailabilityItems = serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom);
        assertEquals(2, actualAvailabilityItems.size());
        assertEquals(availabilityItems.get(1), actualAvailabilityItems.get(0));
        assertEquals(availabilityItems.get(2), actualAvailabilityItems.get(1));
    }

}
