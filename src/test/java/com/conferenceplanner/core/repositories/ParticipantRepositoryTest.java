package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.domain.ParticipantFixture;
import com.conferenceplanner.core.repositories.tools.DatabaseCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void setUp() throws Exception {
        databaseCleaner.clear();
        participant = ParticipantFixture.createParticipant(SURNAME);
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        participantRepository.create(participant);
        assertNotNull(participant.getId());
    }

    @Test
    @Transactional
    public void testGetById() throws Exception {
        participantRepository.create(participant);
        int id = participant.getId();
        assertEquals(participant, participantRepository.getById(id));
    }

    @Test
    @Transactional
    public void testGetAll() throws Exception {
        Participant participant1 = ParticipantFixture.createParticipant(SURNAME);
        Participant participant2 = ParticipantFixture.createParticipant(ANOTHER_SURNAME);
        participantRepository.create(participant1);
        participantRepository.create(participant2);
        List<Participant> participants = participantRepository.getAll();
        assertEquals(2, participants.size());
        assertEquals(participant1, participants.get(0));
        assertEquals(participant2, participants.get(1));
    }
}