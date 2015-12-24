package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.core.services.ParticipantServiceAssistant;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
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


public class ParticipantServiceTest {

    @InjectMocks
    private ParticipantService participantService;

    @Mock
    private ConferenceService conferenceService;

    @Mock
    private ParticipantServiceAssistant serviceAssistant;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void  test_addParticipant() {

    }

    @Test
    public void test_removeParticipant() {
        List<Participant> registeredParticipants = ParticipantFixture.createParticipants(3);
        Participant participant = registeredParticipants.get(0);
        Conference conference = ConferenceFixture.createUpcomingConference();
        when(conferenceService.getConference(anyInt())).thenReturn(conference);
        when(serviceAssistant.getParticipant(anyInt())).thenReturn(participant);
        when(conferenceService.getParticipants(conference)).thenReturn(registeredParticipants);
        participantService.removeParticipant(1, 1);
        verify(serviceAssistant, times(1)).removeParticipant(participant, conference);
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_conference_is_not_upcoming() {
        Conference conference = ConferenceFixture.createOngoingConference();
        when(conferenceService.getConference(anyInt())).thenReturn(conference);
        expectedException.expect(ApplicationException.class);
        participantService.removeParticipant(1, 1);
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_participant_is_not_registered() {
        List<Participant> registeredParticipants = ParticipantFixture.createParticipants(3);
        Participant participant = ParticipantFixture.createParticipant();
        assertFalse(registeredParticipants.contains(participant));
        Conference conference = ConferenceFixture.createUpcomingConference();
        when(conferenceService.getConference(anyInt())).thenReturn(conference);
        when(serviceAssistant.getParticipant(anyInt())).thenReturn(participant);
        when(conferenceService.getParticipants(conference)).thenReturn(registeredParticipants);
        expectedException.expect(ApplicationException.class);
        participantService.removeParticipant(1, 1);
    }
}
