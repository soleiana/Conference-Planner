package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ConferenceRoomRepositoryTest extends SpringContextTest {

    private static final String NAME = "Theatre";
    private static final String ANOTHER_NAME = "University";

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private ConferenceRoom conferenceRoom;

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conferenceRoom = ConferenceRoomFixture.createConferenceRoom(NAME);
    }

    @Test
    public void test_create() {
        conferenceRoomRepository.create(conferenceRoom);
        assertNotNull(conferenceRoom.getId());
    }

    @Test
    public void test_getById() {
        conferenceRoomRepository.create(conferenceRoom);
        int id = conferenceRoom.getId();
        assertEquals(conferenceRoom, conferenceRoomRepository.getById(id));
    }

    @Test
    public void test_getAll() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(NAME);
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(ANOTHER_NAME);
        conferenceRoomRepository.create(room1);
        conferenceRoomRepository.create(room2);
        List<ConferenceRoom> rooms = conferenceRoomRepository.getAll();
        assertEqualData(Arrays.asList(room1, room2), rooms);
    }

    private static void assertEqualData(List<ConferenceRoom> expectedConferenceRooms, List<ConferenceRoom> actualConferenceRooms) {
        assertTrue(expectedConferenceRooms.containsAll(actualConferenceRooms));
        assertEquals(expectedConferenceRooms.size(), actualConferenceRooms.size());
    }
}