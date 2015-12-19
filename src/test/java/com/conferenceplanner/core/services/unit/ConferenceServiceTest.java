package com.conferenceplanner.core.services.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.services.ConferenceChecker;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.core.services.DatabaseException;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
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

public class ConferenceServiceTest {

    @InjectMocks
    private ConferenceService conferenceService;

    @Mock
    private ConferenceRepository conferenceRepository;

    @Mock
    private ConferenceChecker conferenceChecker;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getUpcomingConferences_throws_DatabaseException() {
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getUpcoming();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.getUpcomingConferences();
    }

    @Test
    public void test_getUpcomingConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        when(conferenceRepository.getUpcoming()).thenReturn(upcomingConferences);
        List<Conference> conferences = conferenceService.getUpcomingConferences();
        assertEquals(upcomingConferences.size(), conferences.size());
    }

    @Test
    public void test_getAvailableConferences_throws_DatabaseException() {
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getUpcoming();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.getAvailableConferences();
    }

    @Test
    public void test_getAvailableConferences() {
        List<Conference> upcomingConferences = ConferenceFixture.createUpcomingConferences();
        assertEquals(3, upcomingConferences.size());
        when(conferenceRepository.getUpcoming()).thenReturn(upcomingConferences);
        when(conferenceChecker.isAvailable(upcomingConferences.get(0))).thenReturn(false);
        when(conferenceChecker.isAvailable(upcomingConferences.get(1))).thenReturn(true);
        when(conferenceChecker.isAvailable(upcomingConferences.get(2))).thenReturn(false);

        List<Conference> conferences = conferenceService.getAvailableConferences();
        assertEquals(1, conferences.size());
        assertEquals(upcomingConferences.get(1), conferences.get(0));
    }

    @Test
    public void test_getConference_throws_DatabaseException() {
        final int id = 1;
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getById(id);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.getConference(id);
    }

    @Test
    public void test_getConference() {
        final int id = 1;
        Conference conference = ConferenceFixture.createUpcomingConference();
        when(conferenceRepository.getById(id)).thenReturn(conference);
        Conference actualConference = conferenceService.getConference(id);
        assertEquals(conference, actualConference);
    }

    @Test
    public void test_checkIfConferenceExists_throws_DatabaseException() {
        Conference plannedConference = ConferenceFixture.createConference();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).getUpcoming();
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.checkIfConferenceExists(plannedConference);
    }

    @Test
    public void test_checkIfConferenceExists_is_true_if_planned_conference_exists() {
        Conference conference = ConferenceFixture.createConference();
        conference.setName("name");
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("name");
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_no_conferences_exist() {
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("name");
        plannedConference.setEndDateTime(plannedConference.getEndDateTime().plusMinutes(1));
        List<Conference> emptyList = new ArrayList<>();
        when(conferenceRepository.getUpcoming()).thenReturn(emptyList);
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceExists_is_false_if_planned_conference_is_with_different_name() {
        Conference conference = ConferenceFixture.createConference();
        conference.setName("name");
        Conference plannedConference = ConferenceFixture.createConference();
        plannedConference.setName("anotherName");
        when(conferenceRepository.getUpcoming()).thenReturn(Arrays.asList(conference));
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
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
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
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
        boolean result = conferenceService.checkIfConferenceExists(plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceIsUpcoming_is_true_if_conference_is_upcoming() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        boolean result = conferenceService.checkIfConferenceIsUpcoming(conference);
        assertTrue(result);
    }

    @Test
    public void test_checkIfConferenceIsUpcoming_is_false_if_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        boolean result = conferenceService.checkIfConferenceIsUpcoming(conference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceIsUpcoming_is_false_if_conference_is_ongoing() {
        Conference conference = ConferenceFixture.createOngoingConference();
        boolean result = conferenceService.checkIfConferenceIsUpcoming(conference);
        assertFalse(result);
    }

    @Test
    public void test_checkIfConferenceIsCancelled_is_true_if_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        conference.setCancelled(true);
        boolean result = conferenceService.checkIfConferenceIsCancelled(conference);
        assertTrue(result);
    }

    @Test
    public void test_createConference_throws_DatabaseException() {
        Conference conference = ConferenceFixture.createConference();
        doThrow(new RuntimeException("Database connection failed")).when(conferenceRepository).create(conference);
        expectedException.expect(DatabaseException.class);
        expectedException.expectMessage("Database connection failed");
        conferenceService.createConference(conference);
    }

    @Test
    public void test_createConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        conferenceService.createConference(conference);
        verify(conferenceRepository, times(1)).create(conference);
    }

    @Test
    public void test_cancelConference() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        assertFalse(conference.isCancelled());
        conferenceService.cancelConference(conference);
        assertTrue(conference.isCancelled());
    }

}
