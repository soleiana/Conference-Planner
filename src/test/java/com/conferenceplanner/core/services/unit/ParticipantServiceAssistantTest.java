package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ParticipantRepository;
import com.conferenceplanner.core.services.ParticipantServiceAssistant;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.fixtures.ParticipantFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.*;


public class ParticipantServiceAssistantTest {

    @InjectMocks
    private ParticipantServiceAssistant serviceAssistant;

    @Mock
    private ParticipantRepository participantRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        when(participantRepository.getById(anyInt())).thenReturn(participant);
        Participant actualParticipant = serviceAssistant.getParticipant(1);
        assertEquals(participant, actualParticipant);
    }

    @Test
    public void test_removeParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createPartiallyOccupiedConferenceRooms(rooms.size());
        availabilityItems.get(0).setConferenceRoom(rooms.get(0));
        availabilityItems.get(1).setConferenceRoom(rooms.get(1));
        ConferenceRoomAvailabilityItem availabilityItem = availabilityItems.get(0);
        assertEquals(0, availabilityItem.getAvailableSeats());
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        conference.addParticipant(participant);
        serviceAssistant.removeParticipant(participant, conference);
        assertTrue(conference.getParticipants().isEmpty());
        assertEquals(1, availabilityItem.getAvailableSeats());
    }
}
