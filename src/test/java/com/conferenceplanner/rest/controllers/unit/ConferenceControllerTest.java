package com.conferenceplanner.rest.controllers.unit;

import com.conferenceplanner.core.domain.ConferenceParticipants;
import com.conferenceplanner.core.services.ApplicationErrorCode;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.rest.controllers.ConferenceController;
import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.Conferences;
import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.factories.ConferenceFactory;
import com.conferenceplanner.rest.factories.ConferenceParticipantFactory;
import com.conferenceplanner.rest.fixtures.ConferenceFixture;
import com.conferenceplanner.rest.fixtures.ParticipantFixture;
import com.conferenceplanner.rest.validators.ConferenceValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ConferenceControllerTest {

    @InjectMocks
    private ConferenceController controller;

    @Mock
    private ConferenceService conferenceService;

    @Mock
    private ConferenceValidator conferenceValidator;

    @Mock
    private ConferenceFactory conferenceFactory;

    @Mock
    private ConferenceParticipantFactory conferenceParticipantFactory;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createConference_returns_CREATED() {
        Conference conference = ConferenceFixture.createConference();
        ResponseEntity<String> response = controller.createConference(conference);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_createConference_returns_BAD_REQUEST() {
        Conference conference = ConferenceFixture.createConference();
        doThrow(new ValidationException("")).when(conferenceValidator).validate(conference);
        ResponseEntity<String> response = controller.createConference(conference);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_createConference_returns_CONFLICT() {
        Conference conference = ConferenceFixture.createConference();
        doThrow(new ApplicationException("", ApplicationErrorCode.CONFLICT))
                .when(conferenceService).createConference(any(com.conferenceplanner.core.domain.Conference.class), anyList());
        ResponseEntity<String> response = controller.createConference(conference);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_getUpcomingConferences_returns_OK() {
        List<Conference> conferences = ConferenceFixture.createConferences(2);
        when(conferenceFactory.create(anyList())).thenReturn(conferences);
        ResponseEntity<Conferences> response = controller.getUpcomingConferences();
        assertEquals(conferences.size(), response.getBody().getConferences().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getUpcomingConferences_returns_NOT_FOUND() {
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND))
                .when(conferenceService).getUpcomingConferences();
        ResponseEntity<Conferences> response = controller.getUpcomingConferences();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferences_returns_OK() {
        List<Conference> conferences = ConferenceFixture.createConferences(2);
        when(conferenceFactory.create(anyList())).thenReturn(conferences);
        ResponseEntity<Conferences> response = controller.getAvailableConferences();
        assertEquals(conferences.size(), response.getBody().getConferences().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferences_returns_NOT_FOUND() {
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND))
                .when(conferenceService).getAvailableConferences();
        ResponseEntity<Conferences> response = controller.getAvailableConferences();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getParticipants_returns_OK() {
        List<Participant> participants = ParticipantFixture.createParticipants(2);
        Conference conference = ConferenceFixture.createConference();
        com.conferenceplanner.rest.domain.ConferenceParticipants conferenceParticipants =
                new com.conferenceplanner.rest.domain.ConferenceParticipants();
        conferenceParticipants.setConference(conference);
        conferenceParticipants.setParticipants(participants);
        when(conferenceParticipantFactory.create(any(ConferenceParticipants.class))).thenReturn(conferenceParticipants);
        ResponseEntity<com.conferenceplanner.rest.domain.ConferenceParticipants> response = controller.getParticipants(1);
        assertEquals(participants.size(), response.getBody().getParticipants().size());
        assertNotNull(response.getBody().getConference());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getParticipants_returns_NOT_FOUND() {
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND)).when(conferenceService).getParticipants(anyInt());
        ResponseEntity<com.conferenceplanner.rest.domain.ConferenceParticipants> response = controller.getParticipants(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getParticipants_returns_CONFLICT() {
        doThrow(new ApplicationException("", ApplicationErrorCode.CONFLICT)).when(conferenceService).getParticipants(anyInt());
        ResponseEntity<com.conferenceplanner.rest.domain.ConferenceParticipants> response = controller.getParticipants(1);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_cancelConference_returns_OK() {
        ResponseEntity<String> response = controller.cancelConference(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_cancelConference_returns_NOT_FOUND() {
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND)).when(conferenceService).cancelConference(anyInt());
        ResponseEntity<String> response = controller.cancelConference(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_cancelConference_returns_CONFLICT() {
        doThrow(new ApplicationException("", ApplicationErrorCode.CONFLICT)).when(conferenceService).cancelConference(anyInt());
        ResponseEntity<String> response = controller.cancelConference(1);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
