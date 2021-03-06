package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceChecker;
import com.conferenceplanner.core.services.ConferenceServiceAssistant;
import com.conferenceplanner.core.fixtures.ConferenceFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.fixtures.ParticipantFixture;
import com.conferenceplanner.core.services.unit.helpers.ServiceUnitTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class ConferenceServiceAssistantTest extends SpringContextTest {

    @InjectMocks
    private ConferenceServiceAssistant serviceAssistant;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Mock
    private ConferenceChecker conferenceChecker;

    @Autowired
    private ServiceUnitTestHelper testHelper;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_no_conferences_exist() {
        Conference plannedConference = ConferenceFixture.createUpcomingConference();
        List<Conference> emptyList = new ArrayList<>();
        when(conferenceRepository.getUpcoming()).thenReturn(emptyList);
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_does_not_exist() {
        Conference conference = ConferenceFixture.createConference();
        Conference plannedConference = ConferenceFixture.createConference();
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        when(conferenceChecker.compare(conference, plannedConference)).thenReturn(false);
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_true_if_planned_conference_exists() {
        Conference conference = ConferenceFixture.createConference();
        Conference plannedConference = ConferenceFixture.createConference();
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        when(conferenceChecker.compare(conference, plannedConference)).thenReturn(true);
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_registerConference() {
        Conference conference = ConferenceFixture.createConference();
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRoomsWithId(3);
        assertEquals(3, rooms.size());
        List<Integer> roomIds = testHelper.getConferenceRoomIds(rooms);
        when(conferenceRoomRepository.getById(1)).thenReturn(rooms.get(0));
        when(conferenceRoomRepository.getById(2)).thenReturn(rooms.get(1));
        when(conferenceRoomRepository.getById(3)).thenReturn(rooms.get(2));
        serviceAssistant.registerConference(conference, roomIds);
        testHelper.assertRegisterConferenceResult(conference, rooms);
    }

    @Test
    public void test_getUpcomingConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        when(conferenceRepository.getUpcoming()).thenReturn(upcomingConferences);
        List<Conference> conferences = serviceAssistant.getUpcomingConferences();
        assertEquals(upcomingConferences.size(), conferences.size());
    }

    @Test
    public void test_getAvailableConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        assertEquals(3, upcomingConferences.size());
        when(conferenceRepository.getUpcoming()).thenReturn(upcomingConferences);
        when(conferenceChecker.isAvailable(upcomingConferences.get(0))).thenReturn(false);
        when(conferenceChecker.isAvailable(upcomingConferences.get(1))).thenReturn(true);
        when(conferenceChecker.isAvailable(upcomingConferences.get(2))).thenReturn(false);
        List<Conference> conferences = serviceAssistant.getAvailableConferences();
        assertEquals(1, conferences.size());
        assertEquals(upcomingConferences.get(1), conferences.get(0));
    }

    @Test
    public void test_getParticipants(){
        Conference conference = ConferenceFixture.createUpcomingConference();
        List<Participant> participants = ParticipantFixture.createParticipants(3);
        conference.setParticipants(participants);
        List<Participant> actualParticipants = serviceAssistant.getParticipants(conference);
        assertEquals(participants.size(), actualParticipants.size());
    }

    @Test
    public void test_getConference() {
        final int id = 1;
        Conference conference = ConferenceFixture.createUpcomingConference();
        when(conferenceRepository.getById(id)).thenReturn(conference);
        Conference actualConference = serviceAssistant.getConference(id);
        assertEquals(conference, actualConference);
    }

}
