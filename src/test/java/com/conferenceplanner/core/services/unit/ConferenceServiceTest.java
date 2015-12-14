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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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

}
