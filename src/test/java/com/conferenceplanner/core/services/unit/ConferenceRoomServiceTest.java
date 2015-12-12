package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.core.services.DatabaseException;
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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_checkIfExists_is_false_if_no_conference_room_exists()  {
        ConferenceRoom room = new ConferenceRoom("name3", "location1", 7);
        List<ConferenceRoom> emptyList = new ArrayList<>();
        when(conferenceRoomRepository.getAll()).thenReturn(emptyList);

        boolean result = conferenceRoomService.checkIfExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_conference_room_does_not_exist()  {
        ConferenceRoom room = new ConferenceRoom("name3", "location1", 7);
        when(conferenceRoomRepository.getAll()).thenReturn(getConferenceRoomList());

        boolean result = conferenceRoomService.checkIfExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_true_if_conference_room_exists()  {
        ConferenceRoom room = new ConferenceRoom("name1", "location1", 7);
        when(conferenceRoomRepository.getAll()).thenReturn(getConferenceRoomList());

        boolean result = conferenceRoomService.checkIfExists(room);
        assertTrue(result);
    }

    @Test
    public void test_checkIfExists_throws_DatabaseException()  {
        ConferenceRoom room = new ConferenceRoom("name1", "location1", 7);
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getAll();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");

        conferenceRoomService.checkIfExists(room);
    }

    @Test
    public void test_create() {
        ConferenceRoom room = new ConferenceRoom("name1", "location1", 7);
        conferenceRoomService.create(room);
        verify(conferenceRoomRepository, times(1)).create(room);
    }

    @Test
    public void test_create_throws_DatabaseException() {
        ConferenceRoom room = new ConferenceRoom("name1", "location1", 7);
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).create(room);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceRoomService.create(room);
    }

    private List<ConferenceRoom> getConferenceRoomList() {
        List<ConferenceRoom> rooms = new ArrayList<>();
        rooms.add(new ConferenceRoom("name1", "location1", 5));
        rooms.add(new ConferenceRoom("name2", "location2", 5));
        return rooms;
    }
}