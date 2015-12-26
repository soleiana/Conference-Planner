package com.conferenceplanner.rest.controllers.unit;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.services.ApplicationErrorCode;
import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.rest.controllers.ConferenceRoomController;
import com.conferenceplanner.rest.domain.AvailableConferenceRooms;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.ConferenceRoomAvailability;
import com.conferenceplanner.rest.factories.ConferenceFactory;
import com.conferenceplanner.rest.factories.ConferenceRoomAvailabilityFactory;
import com.conferenceplanner.rest.factories.ConferenceRoomFactory;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.rest.validators.ConferenceRoomValidator;
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


public class ConferenceRoomControllerTest {

    @InjectMocks
    private ConferenceRoomController controller;

    @Mock
    private ConferenceRoomValidator conferenceRoomValidator;

    @Mock
    private ConferenceValidator conferenceValidator;

    @Mock
    private ConferenceRoomFactory conferenceRoomFactory;

    @Mock
    private ConferenceRoomAvailabilityFactory conferenceRoomAvailabilityFactory;

    @Mock
    private ConferenceFactory conferenceFactory;

    @Mock
    private ConferenceRoomService conferenceRoomService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createConferenceRoom_returns_CREATED() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        ResponseEntity<String> response = controller.createConferenceRoom(room);
        assertEquals("Conference room created.", response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_createConferenceRoom_returns_BAD_REQUEST() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new ValidationException("")).when(conferenceRoomValidator).validate(room);
        ResponseEntity<String> response = controller.createConferenceRoom(room);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_createConferenceRoom_returns_CONFLICT() {
        ConferenceRoom room = ConferenceRoomFixture.createConferenceRoom();
        doThrow(new ApplicationException("", ApplicationErrorCode.CONFLICT))
                .when(conferenceRoomService).createConferenceRoom(any(com.conferenceplanner.core.domain.ConferenceRoom.class));
        ResponseEntity<String> response = controller.createConferenceRoom(room);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferenceRooms_returns_OK() {
        ConferenceInterval interval = mock(ConferenceInterval.class);
        when(conferenceValidator.validateDates(anyString(), anyString())).thenReturn(interval);
        List<ConferenceRoom> rooms = ConferenceRoomFixture.createConferenceRooms(2);
        when(conferenceRoomFactory.create(anyList())).thenReturn(rooms);
        ResponseEntity<AvailableConferenceRooms> response = controller.getAvailableConferenceRooms("26/12/2015 12:00", "28/12/2015 12:00");
        assertEquals(rooms.size(), response.getBody().getAvailableConferenceRooms().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferenceRooms_returns_BAD_REQUEST() {
        doThrow(new ValidationException("")).when(conferenceValidator).validateDates(anyString(), anyString());
        ResponseEntity<AvailableConferenceRooms> response = controller.getAvailableConferenceRooms("26/12/2015 12:00", "28/12/2015 12:00");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_getAvailableConferenceRooms_returns_NOT_FOUND() {
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND))
                .when(conferenceRoomService).getAvailableConferenceRooms(any(Conference.class));
        ResponseEntity<AvailableConferenceRooms> response = controller.getAvailableConferenceRooms("26/12/2015 12:00", "28/12/2015 12:00");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getConferenceRoomAvailability_returns_OK() {
        ConferenceRoomAvailability conferenceRoomAvailability = mock(ConferenceRoomAvailability.class);
        when(conferenceRoomAvailabilityFactory.create(any(com.conferenceplanner.core.domain.ConferenceRoomAvailability.class)))
                .thenReturn(conferenceRoomAvailability);
        ResponseEntity<ConferenceRoomAvailability> response = controller.getConferenceRoomAvailability(1);
        assertEquals(conferenceRoomAvailability, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test_getConferenceRoomAvailability_returns_BAD_REQUEST() {
        doThrow(new ValidationException("")).when(conferenceRoomValidator).validateId(anyInt());
        ResponseEntity<ConferenceRoomAvailability> response = controller.getConferenceRoomAvailability(1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_getConferenceRoomAvailability_returns_NOT_FOUND() {
        doThrow(new ApplicationException("", ApplicationErrorCode.NOT_FOUND))
                .when(conferenceRoomService).getConferenceRoomAvailabilityItems(anyInt());
        ResponseEntity<ConferenceRoomAvailability> response = controller.getConferenceRoomAvailability(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
