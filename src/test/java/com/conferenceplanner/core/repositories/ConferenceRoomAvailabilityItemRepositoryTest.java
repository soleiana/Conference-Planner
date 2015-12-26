package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.fixtures.ConferenceFixture;
import com.conferenceplanner.core.repositories.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.repositories.tools.DatabaseConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.Assert.*;

public class ConferenceRoomAvailabilityItemRepositoryTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private DatabaseConfigurator databaseConfigurator;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    private ConferenceRoom conferenceRoom;
    private Conference conference;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conferenceRoom = ConferenceRoomFixture.createConferenceRoom("University");
        conference = ConferenceFixture.createConference("Devoxx 2016");
        databaseConfigurator.configureConferenceRoom(conferenceRoom);
        databaseConfigurator.configureConference(conference);
    }

    @Test
    public void test_create() throws Exception {
        ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem = new ConferenceRoomAvailabilityItem(conferenceRoom.getMaxSeats());
        conferenceRoom.addConferenceRoomAvailabilityItem(conferenceRoomAvailabilityItem);
        conference.addConferenceRoomAvailabilityItem(conferenceRoomAvailabilityItem);
        conferenceRoomAvailabilityItem.setConferenceRoom(conferenceRoom);
        conferenceRoomAvailabilityItem.setConference(conference);
        conferenceRoomAvailabilityItemRepository.create(conferenceRoomAvailabilityItem);
        assertNotNull(conferenceRoomAvailabilityItem.getId());
    }
}