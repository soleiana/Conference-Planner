package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ParticipantRepository;
import com.conferenceplanner.core.services.ParticipantServiceAssistant;
import com.conferenceplanner.core.fixtures.ConferenceFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomAvailabilityItemFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.fixtures.ParticipantFixture;
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
        final int MAX_SEATS = 100;
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2, MAX_SEATS);
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(rooms.size(), MAX_SEATS);
        availabilityItems.get(0).setConferenceRoom(rooms.get(0));
        availabilityItems.get(1).setConferenceRoom(rooms.get(1));
        ConferenceRoomAvailabilityItem availabilityItem = availabilityItems.get(0);
        availabilityItem.takeAvailableSeat();
        assertEquals(MAX_SEATS - 1, availabilityItem.getAvailableSeats());
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        conference.addParticipant(participant);
        serviceAssistant.removeParticipant(participant, conference);
        assertTrue(conference.getParticipants().isEmpty());
        assertEquals(MAX_SEATS, availabilityItem.getAvailableSeats());
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

    @Test
    public void test_registerParticipant() {
        final int MAX_SEATS = 100;
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(2, MAX_SEATS);
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        serviceAssistant.registerParticipant(participant, conference);
        assertEquals(1, conference.getParticipants().size());
        assertEquals(participant, conference.getParticipants().get(0));
        assertEquals(MAX_SEATS - 1, conference.getConferenceRoomAvailabilityItems().get(0).getAvailableSeats());

    }
}
