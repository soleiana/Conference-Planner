package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conferenceRoom = ConferenceRoomFixture.createConferenceRoom(NAME);
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        conferenceRoomRepository.create(conferenceRoom);
        assertNotNull(conferenceRoom.getId());
    }

    @Test
    @Transactional
    public void testGetById() throws Exception {
        conferenceRoomRepository.create(conferenceRoom);
        int id = conferenceRoom.getId();
        assertEquals(conferenceRoom, conferenceRoomRepository.getById(id));
    }

    @Test
    @Transactional
    public void testGetAll() throws Exception {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(NAME);
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(ANOTHER_NAME);
        conferenceRoomRepository.create(room1);
        conferenceRoomRepository.create(room2);
        List<ConferenceRoom> rooms = conferenceRoomRepository.getAll();
        assertEquals(2, rooms.size());
        assertEquals(room1, rooms.get(0));
        assertEquals(room2, rooms.get(1));
    }
}