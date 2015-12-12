package com.conferenceplanner.core.services.component;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import com.conferenceplanner.core.services.ConferenceRoomService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;


public class ConferenceRoomServiceTest extends SpringContextTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
    }

    @Test
    public void test_checkIfExists_is_true_if_conference_room_exists() {
        ConferenceRoom room1 = new ConferenceRoom("name1", "location1", 7);

        conferenceRoomRepository.create(room1);
        assertNotNull(room1.getId());

        ConferenceRoom room2 = new ConferenceRoom("name1", "location1", 6);
        boolean result = conferenceRoomService.checkIfExists(room2);

        assertTrue(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_conference_room_does_not_exist() {
        ConferenceRoom room1 = new ConferenceRoom("name1", "location1", 7);

        conferenceRoomRepository.create(room1);
        assertNotNull(room1.getId());

        ConferenceRoom room2 = new ConferenceRoom("name2", "location1", 6);
        boolean result = conferenceRoomService.checkIfExists(room2);

        assertFalse(result);
    }

    @Test
    public void test_checkIfExists_is_false_if_no_conference_room_exists() {

        ConferenceRoom room = new ConferenceRoom("name1", "location1", 7);
        boolean result = conferenceRoomService.checkIfExists(room);

        assertFalse(result);
    }

    @Test
    public void test_create() {
        ConferenceRoom room = new ConferenceRoom("name1", "location1", 7);
        conferenceRoomService.create(room);
        assertNotNull(room.getId());
    }

}
