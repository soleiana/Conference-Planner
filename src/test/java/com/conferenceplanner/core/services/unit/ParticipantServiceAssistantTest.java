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

import static org.junit.Assert.*;
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
        final int maxSeats = 100;
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2, maxSeats);
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(rooms.size(), maxSeats);
        availabilityItems.get(0).setConferenceRoom(rooms.get(0));
        availabilityItems.get(1).setConferenceRoom(rooms.get(1));
        ConferenceRoomAvailabilityItem availabilityItem = availabilityItems.get(0);
        availabilityItem.takeAvailableSeat();
        assertEquals(maxSeats - 1, availabilityItem.getAvailableSeats());
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        conference.addParticipant(participant);
        serviceAssistant.removeParticipant(participant, conference);
        assertTrue(conference.getParticipants().isEmpty());
        assertEquals(maxSeats, availabilityItem.getAvailableSeats());
    }

    @Test
    public void test_checkIfParticipantIsRegisteredForConference_is_true_if_participant_is_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        conference.addParticipant(participant);
        boolean result = serviceAssistant.checkIfParticipantIsRegisteredForConference(participant, conference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfParticipantIsRegisteredForConference_is_false_if_participant_is_not_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        boolean result = serviceAssistant.checkIfParticipantIsRegisteredForConference(participant, conference);
        assertFalse(result);
    }
}
