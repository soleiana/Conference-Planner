package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.fixtures.ConferenceFixture;
import com.conferenceplanner.core.repositories.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.Assert.*;

public class ConferenceRoomAvailabilityItemRepositoryTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    private ConferenceRoom conferenceRoom;
    private Conference conference;


    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conferenceRoom = ConferenceRoomFixture.createConferenceRoom("University");
        conference = ConferenceFixture.createConference("Devoxx 2016");
        conferenceRoomRepository.create(conferenceRoom);
        conferenceRepository.create(conference);
    }

    @Test
    public void testCreate() throws Exception {
        ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem = new ConferenceRoomAvailabilityItem(conferenceRoom.getMaxSeats());

        conferenceRoom.addConferenceRoomAvailabilityItem(conferenceRoomAvailabilityItem);
        conference.addConferenceRoomAvailabilityItem(conferenceRoomAvailabilityItem);
        conferenceRoomAvailabilityItem.setConferenceRoom(conferenceRoom);
        conferenceRoomAvailabilityItem.setConference(conference);

        conferenceRoomAvailabilityItemRepository.create(conferenceRoomAvailabilityItem);
        assertNotNull(conferenceRoomAvailabilityItem.getId());
    }
}