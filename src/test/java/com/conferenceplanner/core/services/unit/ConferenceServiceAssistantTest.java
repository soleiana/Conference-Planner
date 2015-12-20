package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.services.ConferenceServiceAssistant;
import com.conferenceplanner.core.services.DatabaseException;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.services.unit.helpers.ConferenceServiceUnitTestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Autowired
    private ConferenceServiceUnitTestHelper testHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_checkIfConferenceExists_throws_DatabaseException() {
        Conference plannedConference = ConferenceFixture.createConference();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getUpcoming();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        serviceAssistant.checkIfConferenceExists(plannedConference);
    }

    @Test
    public void test_checkIfConferenceExists_is_true_if_planned_conference_exists() {
        Conference conference = ConferenceFixture.createConference();
        conference.setName("name");
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("name");
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_no_conferences_exist() {
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("name");
        plannedConference.setEndDateTime(plannedConference.getEndDateTime().plusMinutes(1));
        List<Conference> emptyList = new ArrayList<>();
        when(conferenceRepository.getUpcoming()).thenReturn(emptyList);
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_name() {
        Conference conference = ConferenceFixture.createConference();
        conference.setName("name");
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("anotherName");
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_start_date_time() {
        Conference conference = ConferenceFixture.createConference();
        conference.setName("name");
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("name");
        plannedConference.setStartDateTime(plannedConference.getStartDateTime().plusMinutes(1));
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_end_date_time() {
        Conference conference = ConferenceFixture.createConference();
        conference.setName("name");
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("name");
        plannedConference.setEndDateTime(plannedConference.getEndDateTime().plusMinutes(1));
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        boolean result = serviceAssistant.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_registerConference_throws_DatabaseException() {
        Conference conference = ConferenceFixture.createConference();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRoomRepository).getById(1);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        serviceAssistant.registerConference(conference, Arrays.asList(1, 2, 3));
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

}
