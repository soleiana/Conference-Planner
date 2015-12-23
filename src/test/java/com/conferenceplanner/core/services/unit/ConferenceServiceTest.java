package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceParticipants;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.services.*;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ParticipantFixture;
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
    public void test_getUpcomingConferences_throws_AccessException() {
        List<Conference> emptyList = new ArrayList<>();
        when(serviceAssistant.getUpcomingConferences()).thenReturn(emptyList);
        expectedException.expect(AccessException.class);
        conferenceService.getUpcomingConferences();
    }

    @Test
    public void test_getAvailableConferences_throws_AccessException() {
        List<Conference> emptyList = new ArrayList<>();
        when(serviceAssistant.getAvailableConferences()).thenReturn(emptyList);
        expectedException.expect(AccessException.class);
        conferenceService.getAvailableConferences();
    }

    @Test
    public void test_getAvailableConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        when(serviceAssistant.getAvailableConferences()).thenReturn(upcomingConferences);
        List<Conference> conferences = conferenceService.getAvailableConferences();
        assertEquals(upcomingConferences.size(), conferences.size());
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
    public void test_cancelConference_throws_AccessException() {
        int id = 1;
        when(serviceAssistant.getConference(id)).thenReturn(null);
        expectedException.expect(AccessException.class);
        conferenceService.cancelConference(id);
    }

    @Test
    public void test_getParticipants_throws_AccessException_if_conference_does_not_exist(){
        int id = 1;
        when(serviceAssistant.getConference(id)).thenReturn(null);
        expectedException.expect(AccessException.class);
        conferenceService.getParticipants(id);
    }

    @Test
    public void test_getParticipants_throws_AccessException_if_conference_is_not_upcoming(){
        int id = 1;
        Conference conference = ConferenceFixture.createCancelledConference();
        when(serviceAssistant.getConference(id)).thenReturn(conference);
        expectedException.expect(AccessException.class);
        conferenceService.getParticipants(id);
    }

    @Test
    public void test_getParticipants_throws_AccessException_if_no_participant_exists(){
        int id = 1;
        Conference conference = ConferenceFixture.createUpcomingConference();
        when(serviceAssistant.getConference(id)).thenReturn(conference);
        List<Participant> emptyList = new ArrayList<>();
        when(serviceAssistant.getParticipants(conference)).thenReturn(emptyList);
        expectedException.expect(AccessException.class);
        conferenceService.getParticipants(id);
    }

    @Test
    public void test_getParticipants(){
        int id = 1;
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<Participant> participants = ParticipantFixture.createParticipants(3);
        when(serviceAssistant.getConference(id)).thenReturn(conference);
        when(serviceAssistant.getParticipants(conference)).thenReturn(participants);
        ConferenceParticipants conferenceParticipants = conferenceService.getParticipants(id);
        assertEquals(participants.size(), conferenceParticipants.getParticipants().size());
        assertEquals(conference, conferenceParticipants.getConference());
    }

}
