package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.fixtures.ParticipantFixture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ParticipantServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ParticipantService participantService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_addParticipant() {

    }

    @Test
    public void test_removeParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
        rooms.get(0).getConferenceRoomAvailabilityItems().get(0).takeAvailableSeat();
        databaseConfigurator.configure(conference, participant);
        assertEquals(1, conference.getParticipants().size());
        participantService.removeParticipant(participant.getId(), conference.getId());
        assertTrue(conference.getParticipants().isEmpty());
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_conference_does_not_exist() {
        Participant participant = ParticipantFixture.createParticipant();
        databaseConfigurator.configureParticipant(participant);
        expectedException.expect(ApplicationException.class);
        participantService.removeParticipant(participant.getId(), 1);
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_conference_is_not_upcoming() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createOngoingConference();
        databaseConfigurator.configureParticipant(participant);
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        participantService.removeParticipant(participant.getId(), conference.getId());
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_participant_is_not_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureParticipant(participant);
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        participantService.removeParticipant(participant.getId(), conference.getId());
    }

}
