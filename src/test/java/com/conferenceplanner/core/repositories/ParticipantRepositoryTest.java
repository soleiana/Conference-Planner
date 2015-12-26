package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.fixtures.ParticipantFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ParticipantRepositoryTest extends SpringContextTest {

    private static final String SURNAME = "Apse";
    private static final String ANOTHER_SURNAME = "Meitans";

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private Participant participant;

    @Before
    public void setUp() throws Exception {
        databaseCleaner.clear();
        participant = ParticipantFixture.createParticipant(SURNAME);
    }

    @Test
    public void test_create() {
        participantRepository.create(participant);
        assertNotNull(participant.getId());
    }

    @Test
    public void test_getById() {
        participantRepository.create(participant);
        int id = participant.getId();
        assertEquals(participant, participantRepository.getById(id));
    }

    @Test
    public void test_getAll() {
        Participant participant1 = ParticipantFixture.createParticipant(SURNAME);
        Participant participant2 = ParticipantFixture.createParticipant(ANOTHER_SURNAME);
        participantRepository.create(participant1);
        participantRepository.create(participant2);
        List<Participant> participants = participantRepository.getAll();
        assertEqualData(Arrays.asList(participant1, participant2), participants);
    }

    private static void assertEqualData(List<Participant> expectedParticipants, List<Participant> actualParticipants) {
        assertTrue(expectedParticipants.containsAll(actualParticipants));
        assertEquals(expectedParticipants.size(), actualParticipants.size());
    }
}