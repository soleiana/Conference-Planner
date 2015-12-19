package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.*;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.unit.helpers.ConferenceRoomServiceUnitTestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConferenceRoomServiceTest extends SpringContextTest {

    @InjectMocks
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Mock
    private ConferenceRoomChecker conferenceRoomChecker;

    @Mock
    private ConferenceRoomAvailabilityItemChecker conferenceRoomAvailabilityItemChecker;

    @Autowired
    private ConferenceRoomServiceUnitTestHelper conferenceRoomServiceUnitTestHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_no_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoom> emptyList = new ArrayList<>();
        when(conferenceRoomRepository.getAll()).thenReturn(emptyList);
        boolean result = conferenceRoomService.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_conference_room_does_not_exist()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        when(conferenceRoomRepository.getAll()).thenReturn(ConferenceRoomFixture.createConferenceRooms());
        boolean result = conferenceRoomService.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_true_if_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom(7);
        when(conferenceRoomRepository.getAll()).thenReturn(ConferenceRoomFixture.createConferenceRooms());
        boolean result = conferenceRoomService.checkIfConferenceRoomExists(room);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_throws_DatabaseException()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getAll();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.checkIfConferenceRoomExists(room);
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        conferenceRoomService.createConferenceRoom(room);
        verify(conferenceRoomRepository, times(1)).create(room);
    }

    @Test
    public void test_createConferenceRoom_throws_DatabaseException() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).create(room);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.createConferenceRoom(room);
    }

    @Test
    public void test_getAvailableConferenceRooms_throws_DatabaseException()  {
        Conference plannedConference = ConferenceFixture.createConference();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getAll();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.getAvailableConferenceRooms(plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        assertEquals(2, rooms.size());
        when(conferenceRoomRepository.getAll()).thenReturn(rooms);
        when(conferenceRoomChecker.isAvailable(rooms.get(0),plannedConference)).thenReturn(false);
        when(conferenceRoomChecker.isAvailable(rooms.get(1),plannedConference)).thenReturn(true);

        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(1, availableRooms.size());
        assertEquals(rooms.get(1), availableRooms.get(0));
    }

    @Test
    public void test_checkIfConferenceRoomsAvailable_is_true_when_all_checked_rooms_are_available()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRoomsWithId(3);
        List<Integer> roomIds = rooms.stream()
                .map(ConferenceRoom::getId)
                .collect(Collectors.toList());
        assertEquals(3, rooms.size());
        when(conferenceRoomRepository.getAll()).thenReturn(rooms);
        when(conferenceRoomChecker.isAvailable(rooms.get(0),plannedConference)).thenReturn(true);
        when(conferenceRoomChecker.isAvailable(rooms.get(1),plannedConference)).thenReturn(true);
        when(conferenceRoomChecker.isAvailable(rooms.get(2),plannedConference)).thenReturn(true);

        boolean result = conferenceRoomService.checkIfConferenceRoomsAvailable(roomIds, plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceRoomsAvailable_is_false_when_one_checked_room_is_unavailable()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRoomsWithId(3);
        List<Integer> roomIds = rooms.stream()
                .map(ConferenceRoom::getId)
                .collect(Collectors.toList());
        assertEquals(3, rooms.size());
        when(conferenceRoomRepository.getAll()).thenReturn(rooms);
        when(conferenceRoomChecker.isAvailable(rooms.get(0),plannedConference)).thenReturn(true);
        when(conferenceRoomChecker.isAvailable(rooms.get(1),plannedConference)).thenReturn(false);
        when(conferenceRoomChecker.isAvailable(rooms.get(2),plannedConference)).thenReturn(true);

        boolean result = conferenceRoomService.checkIfConferenceRoomsAvailable(roomIds, plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_throws_DatabaseException()  {
        ConferenceRoom conferenceRoom = mock(ConferenceRoom.class);
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoom).getConferenceRoomAvailabilityItems();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.getConferenceRoomAvailabilityItems(conferenceRoom);
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

        List<ConferenceRoomAvailabilityItem> actualAvailabilityItems = conferenceRoomService.getConferenceRoomAvailabilityItems(conferenceRoom);
        assertEquals(2, actualAvailabilityItems.size());
        assertEquals(availabilityItems.get(1), actualAvailabilityItems.get(0));
        assertEquals(availabilityItems.get(2), actualAvailabilityItems.get(1));
    }

    @Test
    public void test_registerConference_throws_DatabaseException() {
        Conference conference = ConferenceFixture.createConference();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getById(1);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.registerConference(conference, Arrays.asList(1, 2, 3));

    }

    @Test
    public void test_registerConference() {
        Conference conference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRoomsWithId(3);
        assertEquals(3, rooms.size());
        List<Integer> roomIds = rooms.stream()
                .map(ConferenceRoom::getId)
                .collect(Collectors.toList());
        when(conferenceRoomRepository.getById(1)).thenReturn(rooms.get(0));
        when(conferenceRoomRepository.getById(2)).thenReturn(rooms.get(1));
        when(conferenceRoomRepository.getById(3)).thenReturn(rooms.get(2));

        conferenceRoomService.registerConference(conference, roomIds);
        conferenceRoomServiceUnitTestHelper.assertRegisterConferenceResult(conference, rooms);
    }

}