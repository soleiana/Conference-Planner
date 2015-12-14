package com.conferenceplanner.core.services.component;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class ConferenceServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceService conferenceService;

    private LocalDateTime now;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        now = LocalDateTime.now();
    }

    @Test
    public void test_getUpcomingConferences_if_upcoming_conferences() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        databaseConfigurator.configure(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertEquals(conferences.size(), upcomingConferences.size());
        assertResult(upcomingConferences);
    }

    @Test
    public void test_getUpcomingConferences_if_cancelled_conferences() {
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        databaseConfigurator.configure(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertTrue(upcomingConferences.isEmpty());
    }

    @Test
    public void test_getUpcomingConferences_if_cancelled_conference() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setCancelled(true);
        databaseConfigurator.configure(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertEquals(conferences.size()-1, upcomingConferences.size());
        assertResult(upcomingConferences);
    }

    @Test
    public void test_getUpcomingConferences_if_conference_in_past() {
        List<Conference> conferences = ConferenceFixture.createUpcomingConferences();
        conferences.get(0).setStartDateTime(LocalDateTime.now().minusMinutes(1));
        databaseConfigurator.configure(conferences);
        List<Conference> upcomingConferences = conferenceService.getUpcomingConferences();
        assertEquals(conferences.size()-1, upcomingConferences.size());
        assertResult(upcomingConferences);
    }

    private void assertResult(List<Conference> conferences) {
        for (Conference conference: conferences) {
            assertFalse(conference.isCancelled());
            assertTrue(conference.getStartDateTime().isAfter(now));
        }
    }

}
