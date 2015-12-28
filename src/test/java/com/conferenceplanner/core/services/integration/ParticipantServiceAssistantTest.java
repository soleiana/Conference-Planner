package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ParticipantServiceAssistant;
import com.conferenceplanner.core.fixtures.ConferenceFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.fixtures.ParticipantFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.junit.Assert.*;


public class ParticipantServiceAssistantTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ParticipantServiceAssistant serviceAssistant;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_getParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        databaseConfigurator.configureParticipant(participant);
        assertNotNull(participant.getId());
        Participant actualParticipant = serviceAssistant.getParticipant(participant.getId());
        assertEquals(participant, actualParticipant);
    }

    @Test
    public void test_removeParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configure(rooms, conference);
        ConferenceRoom room = rooms.get(0);
        ConferenceRoomAvailabilityItem availabilityItem =  room.getConferenceRoomAvailabilityItems().get(0);
        availabilityItem.takeAvailableSeat();
        assertEquals(room.getMaxSeats() - 1, availabilityItem.getAvailableSeats());
        databaseConfigurator.configure(conference, participant);
        assertEquals(1, conference.getParticipants().size());
        serviceAssistant.removeParticipant(participant, conference);
        assertTrue(conference.getParticipants().isEmpty());
        assertEquals(room.getMaxSeats(), availabilityItem.getAvailableSeats());
    }

    @Test
    public void test_checkIfParticipantIsRegisteredForConference_is_true_if_participant_is_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configure(conference, participant);
        boolean result = serviceAssistant.checkIfParticipantIsRegisteredForConference(participant, conference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfParticipantIsRegisteredForConference_is_false_if_participant_is_not_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureConference(conference);
        databaseConfigurator.configureParticipant(participant);
        boolean result = serviceAssistant.checkIfParticipantIsRegisteredForConference(participant, conference);
        assertFalse(result);
    }

    @Test
    public void test_createParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        serviceAssistant.createParticipant(participant);
        assertNotNull(participant.getId());
    }

    @Test
    public void test_registerParticipant() {
        final int MAX_SEATS = 100;
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2, MAX_SEATS);
        databaseConfigurator.configure(rooms, conference);
        databaseConfigurator.configureParticipant(participant);
        serviceAssistant.registerParticipant(participant, conference);
        assertEquals(1, conference.getParticipants().size());
        assertEquals(participant, conference.getParticipants().get(0));
        assertEquals(MAX_SEATS - 1, conference.getConferenceRoomAvailabilityItems().get(0).getAvailableSeats());
    }

}
