package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.services.DatabaseException;
import com.conferenceplanner.core.services.ParticipantService;
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


public class ParticipantServiceTest {

    @InjectMocks
    private ParticipantService participantService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getParticipants_throws_DatabaseException(){
        Conference conference = mock(Conference.class);
        doThrow(new RuntimeException("Database connection failed")).when(conference).getParticipants();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        participantService.getParticipants(conference);
    }

    @Test
    public void test_getParticipants(){
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<Participant> participants = ParticipantFixture.createParticipants(3);
        conference.setParticipants(participants);
        List<Participant> actualParticipants = participantService.getParticipants(conference);
        assertEquals(participants.size(), actualParticipants.size());
    }
}
