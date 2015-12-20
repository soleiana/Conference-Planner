package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.services.*;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConferenceServiceTest {

    @InjectMocks
    private ConferenceService conferenceService;

    @Mock
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private ConferenceServiceAssistant serviceAssistant;

    @Mock
    private ConferenceChecker conferenceChecker;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getUpcomingConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        when(serviceAssistant.getUpcomingConferences()).thenReturn(upcomingConferences);
        List<Conference> conferences = conferenceService.getUpcomingConferences();
        assertEquals(upcomingConferences.size(), conferences.size());
    }

    @Test
    public void test_getUpcomingConferences_throws_AccessException_if_no_upcoming_conferences() {
        List<Conference> emptyList = new ArrayList<>();
        when(serviceAssistant.getUpcomingConferences()).thenReturn(emptyList);
        expectedException.expect(AccessException.class);
        conferenceService.getUpcomingConferences();

    }

    @Test
    public void test_getAvailableConferences_throws_DatabaseException() {
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getUpcoming();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.getAvailableConferences();
    }

    @Test
    public void test_getAvailableConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        assertEquals(3, upcomingConferences.size());
        when(conferenceRepository.getUpcoming()).thenReturn(upcomingConferences);
        when(conferenceChecker.isAvailable(upcomingConferences.get(0))).thenReturn(false);
        when(conferenceChecker.isAvailable(upcomingConferences.get(1))).thenReturn(true);
        when(conferenceChecker.isAvailable(upcomingConferences.get(2))).thenReturn(false);

        List<Conference> conferences = conferenceService.getAvailableConferences();
        assertEquals(1, conferences.size());
        assertEquals(upcomingConferences.get(1), conferences.get(0));
    }

    @Test
    public void test_getConference_throws_DatabaseException() {
        final int id = 1;
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getById(id);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.getConference(id);
    }

    @Test
    public void test_getConference() {
        final int id = 1;
        Conference conference = ConferenceFixture.createUpcomingConference();
        when(conferenceRepository.getById(id)).thenReturn(conference);
        Conference actualConference = conferenceService.getConference(id);
        assertEquals(conference, actualConference);
    }

    @Test
    public void test_createConference_throws_AccessException_if_conference_exists() {
        Conference conference = ConferenceFixture.createConference();
        List<Integer> roomIds = Arrays.asList(1, 2, 3);
        when(serviceAssistant.checkIfConferenceExists(conference)).thenReturn(true);
        expectedException.expect(AccessException.class);
        conferenceService.createConference(conference, roomIds);
    }

    @Test
    public void test_createConference_throws_AccessException_if_conference_rooms_are_not_available() {
        Conference conference = ConferenceFixture.createConference();
        List<Integer> roomIds = Arrays.asList(1, 2, 3);
        when(conferenceRoomService.checkIfConferenceRoomsAvailable(roomIds, conference)).thenReturn(false);
        expectedException.expect(AccessException.class);
        conferenceService.createConference(conference, roomIds);
    }

    @Test
    public void test_createConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<Integer> roomIds = Arrays.asList(1, 2, 3);
        when(serviceAssistant.checkIfConferenceExists(conference)).thenReturn(false);
        when(conferenceRoomService.checkIfConferenceRoomsAvailable(roomIds, conference)).thenReturn(true);
        conferenceService.createConference(conference, roomIds);
        verify(serviceAssistant, times(1)).registerConference(conference, roomIds);
    }

    @Test
    public void test_cancelConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        assertFalse(conference.isCancelled());
        conferenceService.cancelConference(conference);
        assertTrue(conference.isCancelled());
    }

}
