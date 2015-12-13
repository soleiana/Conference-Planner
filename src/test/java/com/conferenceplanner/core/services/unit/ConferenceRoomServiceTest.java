package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceRoomChecker;
import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.core.services.DatabaseException;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConferenceRoomServiceTest {

    @InjectMocks
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private ConferenceRoomChecker conferenceRoomChecker;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_checkIfExists_is_false_if_no_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoom> emptyList = new ArrayList<>();
        when(conferenceRoomRepository.getAll()).thenReturn(emptyList);
        boolean result = conferenceRoomService.checkIfExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_conference_room_does_not_exist()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        when(conferenceRoomRepository.getAll()).thenReturn(ConferenceRoomFixture.createConferenceRooms());
        boolean result = conferenceRoomService.checkIfExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_true_if_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom(7);
        when(conferenceRoomRepository.getAll()).thenReturn(ConferenceRoomFixture.createConferenceRooms());
        boolean result = conferenceRoomService.checkIfExists(room);
        assertTrue(result);
    }

    @Test
    public void test_checkIfExists_throws_DatabaseException()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getAll();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.checkIfExists(room);
    }

    @Test
    public void test_create() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        conferenceRoomService.create(room);
        verify(conferenceRoomRepository, times(1)).create(room);
    }

    @Test
    public void test_create_throws_DatabaseException() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).create(room);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.create(room);
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
        when(conferenceRoomRepository.getAll()).thenReturn(rooms);
        when(conferenceRoomChecker.isAvailable(rooms.get(0),plannedConference)).thenReturn(false);
        when(conferenceRoomChecker.isAvailable(rooms.get(1),plannedConference)).thenReturn(true);

        List<ConferenceRoom> availableRooms = conferenceRoomService.getAvailableConferenceRooms(plannedConference);
        assertEquals(1, availableRooms.size());
        assertEquals(rooms.get(1), availableRooms.get(0));
    }

}