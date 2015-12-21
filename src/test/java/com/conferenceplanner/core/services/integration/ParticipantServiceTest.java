package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import com.conferenceplanner.core.services.fixtures.ParticipantFixture;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_addParticipant() {

    }
}
