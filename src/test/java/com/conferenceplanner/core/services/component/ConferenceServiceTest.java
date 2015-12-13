package com.conferenceplanner.core.services.component;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConferenceServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceRepository conferenceRepository;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_getUpcomingConferences_if_upcoming_conferences() {

    }

    @Test
    public void test_getUpcomingConferences_if_cancelled_conferences() {

    }

    @Test
    public void test_getUpcomingConferences_if_cancelled_conference() {

    }

    @Test
    public void test_getUpcomingConferences_if_conference_in_past() {

    }


}
