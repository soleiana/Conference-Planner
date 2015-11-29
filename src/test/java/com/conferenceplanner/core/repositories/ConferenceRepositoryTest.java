package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceFixture;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

public class ConferenceRepositoryTest extends SpringContextTest {

    private static final String NAME = "Devoxx";
    private static final String ANOTHER_NAME = "Jfokus";

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    private Conference conference;

    @Before
    @Transactional
    public void setUp() throws Exception {
        databaseCleaner.clear();
        conference = ConferenceFixture.createConference(NAME);
        setupConference(conference);
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        conferenceRepository.create(conference);
        assertNotNull(conference.getId());
    }

    @Test
    @Transactional
    public void testGetById() throws Exception {
        conferenceRepository.create(conference);
        int id = conference.getId();
        assertEquals(conference, conferenceRepository.getById(id));
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        conferenceRepository.create(conference);
        assertFalse(conference.isCancelled());
        conference.setCancelled(true);
        conferenceRepository.update(conference);
        int id = conference.getId();
        assertTrue(conferenceRepository.getById(id).isCancelled());
    }

    @Test
    @Transactional
    public void testGetAllAvailable() throws Exception {
        List<Conference> conferences = ConferenceFixture.createInputData();
        setupConferences(conferences);
        persistConferences(conferences);
        List<Conference> actualConferences = conferenceRepository.getAllAvailable();
        List<Conference> expectedConferences = ConferenceFixture.createExpectedData();
        assertEquals(expectedConferences.size(), actualConferences.size());
        assertEqualData(expectedConferences, actualConferences);
    }

    @Test
    @Transactional
    public void testGetAvailableByConferenceRoom() throws Exception {

    }

    private void setupConference(Conference conference) {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom("University");
        conferenceRoomRepository.create(conferenceRoom);
        conference.setConferenceRoom(conferenceRoom);
    }

    private void setupConferences(List<Conference> conferences) {
        for (Conference conference: conferences) {
            setupConference(conference);
        }
    }

    private void persistConferences(List<Conference> conferences) {
        for (Conference conference: conferences) {
            conferenceRepository.create(conference);
        }
    }

    private static void assertEqualData(List<Conference> expectedConferences, List<Conference> actualConferences) {
        int dataSize = expectedConferences.size();
        int hitCount = 0;
        for (Conference actualConference: actualConferences) {
            for (Conference expectedConference: expectedConferences) {
                if (actualConference.equals(expectedConference)) {
                    hitCount++;
                }
            }
        }
        assertEquals(dataSize, hitCount);
    }
}