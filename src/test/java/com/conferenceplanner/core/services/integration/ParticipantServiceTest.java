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
    public void test_addParticipant_if_participant_does_not_exist() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
        int availableSeatsBefore =  rooms.get(0).getConferenceRoomAvailabilityItems().get(0).getAvailableSeats();
        participantService.addParticipant(participant, conference.getId());
        assertTrue(conference.getParticipants().contains(participant));
        int availableSeatsAfter = rooms.get(0).getConferenceRoomAvailabilityItems().get(0).getAvailableSeats();
        assertEquals(availableSeatsBefore - 1, availableSeatsAfter);
    }

    @Test
    public void test_addParticipant_if_participant_exists() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
        databaseConfigurator.configureParticipant(participant);
        int availableSeatsBefore =  rooms.get(0).getConferenceRoomAvailabilityItems().get(0).getAvailableSeats();
        participantService.addParticipant(participant, conference.getId());
        assertTrue(conference.getParticipants().contains(participant));
        int availableSeatsAfter = rooms.get(0).getConferenceRoomAvailabilityItems().get(0).getAvailableSeats();
        assertEquals(availableSeatsBefore - 1, availableSeatsAfter);
    }

    @Test
    public void test_addParticipant_throws_ApplicationException_if_no_conferences_are_available() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createOngoingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No available conferences!");
        participantService.addParticipant(participant, conference.getId());
    }

    @Test
    public void test_addParticipant_throws_ApplicationException_if_conference_is_not_available() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference1 = ConferenceFixture.createCancelledConference();
        Conference conference2 = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference1);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference2);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference is not available for registration!");
        participantService.addParticipant(participant, conference1.getId());
    }

    @Test
    public void test_addParticipant_throws_ApplicationException_if_participant_is_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
        databaseConfigurator.configure(conference, participant);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Participant already registered for conference!");
        participantService.addParticipant(participant, conference.getId());
    }

    @Test
    public void test_removeParticipant() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(3);
        databaseConfigurator.configureWithConferenceRoomAvailability(rooms, conference);
        rooms.get(0).getConferenceRoomAvailabilityItems().get(0).takeAvailableSeat();
        int availableSeatsBefore =  rooms.get(0).getConferenceRoomAvailabilityItems().get(0).getAvailableSeats();
        databaseConfigurator.configure(conference, participant);
        assertEquals(1, conference.getParticipants().size());
        participantService.removeParticipant(participant.getId(), conference.getId());
        assertTrue(conference.getParticipants().isEmpty());
        int availableSeatsAfter = rooms.get(0).getConferenceRoomAvailabilityItems().get(0).getAvailableSeats();
        assertEquals(availableSeatsBefore + 1, availableSeatsAfter);
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_conference_does_not_exist() {
        Participant participant = ParticipantFixture.createParticipant();
        databaseConfigurator.configureParticipant(participant);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No conference found for selected id!");
        participantService.removeParticipant(participant.getId(), 1);
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_conference_is_not_upcoming() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createOngoingConference();
        databaseConfigurator.configureParticipant(participant);
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Conference is not upcoming!");
        participantService.removeParticipant(participant.getId(), conference.getId());
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_no_participant_is_registered() {
        Participant participant = ParticipantFixture.createParticipant();
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configureParticipant(participant);
        databaseConfigurator.configureConference(conference);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No participants found!");
        participantService.removeParticipant(participant.getId(), conference.getId());
    }

    @Test
    public void test_removeParticipant_throws_ApplicationException_if_participant_is_not_registered() {
        Participant participant1 = ParticipantFixture.createParticipant("Anna");
        Participant participant2 = ParticipantFixture.createParticipant("Maris");
        participant2.setId(0);
        Conference conference = ConferenceFixture.createUpcomingConference();
        databaseConfigurator.configure(conference, participant1);
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("No participant with selected id registered in conference!");
        participantService.removeParticipant(participant2.getId(), conference.getId());
    }

}
