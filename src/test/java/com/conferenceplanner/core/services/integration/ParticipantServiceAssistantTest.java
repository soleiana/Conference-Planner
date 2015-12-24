package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ParticipantServiceAssistant;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.fixtures.ParticipantFixture;
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
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
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

}
