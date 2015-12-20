package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceRoomServiceAssistant;
import com.conferenceplanner.core.services.DatabaseException;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ConferenceRoomServiceAssistantTest {

    @InjectMocks
    private ConferenceRoomServiceAssistant serviceAssistant;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

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
    public void test_checkIfConferenceRoomExists_is_false_if_no_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        List<ConferenceRoom> emptyList = new ArrayList<>();
        when(conferenceRoomRepository.getAll()).thenReturn(emptyList);
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_false_if_conference_room_does_not_exist()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        when(conferenceRoomRepository.getAll()).thenReturn(ConferenceRoomFixture.createConferenceRooms());
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_is_true_if_conference_room_exists()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom(7);
        when(conferenceRoomRepository.getAll()).thenReturn(ConferenceRoomFixture.createConferenceRooms());
        boolean result = serviceAssistant.checkIfConferenceRoomExists(room);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceRoomExists_throws_DatabaseException()  {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getAll();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        serviceAssistant.checkIfConferenceRoomExists(room);
    }

}
