package com.conferenceplanner.rest.controllers.integration;

import com.conferenceplanner.config.DatasourceConfig;
import com.conferenceplanner.config.RestConfig;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import com.conferenceplanner.rest.controllers.TestUtilController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, DatasourceConfig.class})
public class TestUtilControllerTest {

    @Autowired
    private TestUtilController controller;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private DatabaseCleaner databaseCleaner;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_cleanUpDatabase_returns_OK() {
        setUpDatabase();
        ResponseEntity<String> response = controller.cleanUpDatabase();
        assertEquals("Database cleaned up.", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    public void setUpDatabase() {
        com.conferenceplanner.core.domain.Conference coreDomainConference = com.conferenceplanner.core.fixtures.ConferenceFixture.createUpcomingConference();
        ConferenceRoom coreDomainRoom = ConferenceRoomFixture.createConferenceRoom();
        com.conferenceplanner.core.domain.Participant coreDomainParticipant = com.conferenceplanner.core.fixtures.ParticipantFixture.createParticipant();
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainRoom, coreDomainConference);
        databaseConfigurator.configureWithConferenceRoomAvailability(coreDomainConference, coreDomainParticipant);
    }
}
