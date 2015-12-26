package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.repositories.fixtures.ConferenceFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.junit.Assert.*;

public class ConferenceRepositoryTest extends SpringContextTest {

    private static final String NAME = "Devoxx";

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceRepository conferenceRepository;

    private Conference conference;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conference = ConferenceFixture.createConference(NAME);
    }

    @Test
    public void test_create() {
        conferenceRepository.create(conference);
        assertNotNull(conference.getId());
    }

    @Test
    public void test_getById() {
        conferenceRepository.create(conference);
        int id = conference.getId();
        assertEquals(conference, conferenceRepository.getById(id));
    }

    @Test
    public void test_getUpcoming() {
        List<Conference> conferences = ConferenceFixture.createInputData();
        databaseConfigurator.configureConferences(conferences);
        List<Conference> actualConferences = conferenceRepository.getUpcoming();
        List<Conference> expectedConferences = ConferenceFixture.createExpectedData();
        assertEqualData(expectedConferences, actualConferences);
    }

    private static void assertEqualData(List<Conference> expectedConferences, List<Conference> actualConferences) {
        assertTrue(expectedConferences.containsAll(actualConferences));
        assertEquals(expectedConferences.size(), actualConferences.size());
    }
}