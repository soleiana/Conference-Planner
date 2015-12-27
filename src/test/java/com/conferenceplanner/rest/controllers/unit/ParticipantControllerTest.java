package com.conferenceplanner.rest.controllers.unit;

import com.conferenceplanner.core.services.ApplicationErrorCode;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.rest.controllers.ParticipantController;
import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.factories.ParticipantFactory;
import com.conferenceplanner.rest.fixtures.ParticipantFixture;
import com.conferenceplanner.rest.validators.ParticipantValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ParticipantControllerTest {

    @InjectMocks
    private ParticipantController controller;

    @Mock
    private ParticipantValidator participantValidator;

    @Mock
    private ParticipantService participantService;

     @Mock
    private ParticipantFactory participantFactory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addParticipant_returns_CREATED() {
        Participant participant = ParticipantFixture.createParticipant(1);
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_BAD_REQUEST() {
        Participant participant = ParticipantFixture.createParticipant(1);
        doThrow(new ValidationException("")).when(participantValidator).validate(participant);
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_NOT_FOUND() {
        Participant participant = ParticipantFixture.createParticipant(1);
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND))
                .when(participantService).addParticipant(any(com.conferenceplanner.core.domain.Participant.class), anyInt());
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_addParticipant_returns_CONFLICT() {
        Participant participant = ParticipantFixture.createParticipant(1);
        doThrow(new ApplicationException("", ApplicationErrorCode.CONFLICT))
                .when(participantService).addParticipant(any(com.conferenceplanner.core.domain.Participant.class), anyInt());
        ResponseEntity<String> response = controller.addParticipant(participant);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_OK() {
        Participant participant = ParticipantFixture.createParticipant(1, 1);
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_BAD_REQUEST() {
        Participant participant = ParticipantFixture.createParticipant(1, 1);
        doThrow(new ValidationException("")).when(participantValidator).validateIds(participant);
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_NOT_FOUND() {
        Participant participant = ParticipantFixture.createParticipant(1, 1);
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND)).when(participantService).removeParticipant(anyInt(), anyInt());
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_removeParticipant_returns_CONFLICT() {
        Participant participant = ParticipantFixture.createParticipant(1, 1);
        doThrow(new ApplicationException("", ApplicationErrorCode.CONFLICT)).when(participantService).removeParticipant(anyInt(), anyInt());
        ResponseEntity<String> response = controller.removeParticipant(participant);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
