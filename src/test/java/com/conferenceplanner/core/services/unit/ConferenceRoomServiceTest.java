package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailability;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.*;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.unit.helpers.ServiceUnitTestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConferenceRoomServiceTest extends SpringContextTest {

    @InjectMocks
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private ConferenceRoomServiceAssistant serviceAssistant;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Mock
    private ConferenceRoomChecker conferenceRoomChecker;

    @Autowired
    private ServiceUnitTestHelper testHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        when(serviceAssistant.checkIfConferenceRoomExists(room)).thenReturn(false);
        conferenceRoomService.createConferenceRoom(room);
        verify(serviceAssistant, times(1)).createConferenceRoom(room);
    }

    @Test
    public void test_createConferenceRoom_throws_ApplicationException() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        when(serviceAssistant.checkIfConferenceRoomExists(room)).thenReturn(true);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference room already exists!");
        conferenceRoomService.createConferenceRoom(room);
    }

    @Test
    public void test_getAvailableConferenceRooms_throws_ApplicationException()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> emptyRoomList = new ArrayList<>();
        when(conferenceRoomRepository.getAll()).thenReturn(emptyRoomList);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No conference rooms found for selected conference interval!");
        conferenceRoomService.getAvailableConferenceRooms(plannedConference);
    }

    @Test
    public void test_getAvailableConferenceRooms()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms();
        assertEquals(2, rooms.size());
        when(serviceAssistant.getAvailableConferenceRooms(plannedConference)).thenReturn(rooms);
        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(rooms.size(), availableRooms.size());
    }

    @Test
    public void test_checkIfConferenceRoomsAreAvailable_is_true_if_all_checked_rooms_are_available()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRoomsWithId(3);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        when(serviceAssistant.getAvailableConferenceRooms(plannedConference)).thenReturn(rooms);
        boolean result = conferenceRoomService.checkIfConferenceRoomsAreAvailable(roomIds, plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceRoomsAreAvailable_is_false_if_one_checked_room_is_not_available()  {
        Conference plannedConference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRoomsWithId(3);
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        rooms.remove(rooms.get(0));
        when(serviceAssistant.getAvailableConferenceRooms(plannedConference)).thenReturn(rooms);
        boolean result = conferenceRoomService.checkIfConferenceRoomsAreAvailable(roomIds, plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_throws_ApplicationException_if_conference_room_does_not_exist()  {
        int id = 1;
        when(serviceAssistant.getConferenceRoom(id)).thenReturn(null);
        expectedException.expect(ApplicationException.class);
        conferenceRoomService.getConferenceRoomAvailabilityItems(id);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems_throws_ApplicationException_if_no_conference_is_registered()  {
        int id = 1;
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoomAvailabilityItem> emptyList = new ArrayList<>();
        when(serviceAssistant.getConferenceRoom(id)).thenReturn(conferenceRoom);
        when(serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom)).thenReturn(emptyList);
        expectedException.expect(ApplicationException.class);
        conferenceRoomService.getConferenceRoomAvailabilityItems(id);
    }

    @Test
    public void test_getConferenceRoomAvailabilityItems()  {
        int id = 1;
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(2);
        conferenceRoom.setConferenceRoomAvailabilityItems(availabilityItems);
        when(serviceAssistant.getConferenceRoom(id)).thenReturn(conferenceRoom);
        when(serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom)).thenReturn(availabilityItems);
        ConferenceRoomAvailability availability = conferenceRoomService.getConferenceRoomAvailabilityItems(id);
        assertEquals(2, availability.getAvailabilityItems().size());
        assertEquals(availabilityItems.get(0), availability.getAvailabilityItems().get(0));
        assertEquals(availabilityItems.get(1), availability.getAvailabilityItems().get(1));
    }

}