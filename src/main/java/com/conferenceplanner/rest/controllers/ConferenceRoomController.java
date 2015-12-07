package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.rest.domain.*;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.ConferenceRoomAvailability;
import com.conferenceplanner.rest.factories.ConferenceRoomFactory;
import com.conferenceplanner.rest.validators.ConferenceRoomValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/conference-rooms")
public class ConferenceRoomController {

    @Autowired
    private ConferenceRoomValidator conferenceRoomValidator;

    @Autowired
    private ConferenceRoomFactory conferenceRoomFactory;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "application/json")
    public ResponseEntity<String> createConferenceRoom(@RequestBody ConferenceRoom conferenceRoom) {
        try {
            conferenceRoomValidator.validate(conferenceRoom);

            com.conferenceplanner.core.domain.ConferenceRoom coreDomainConferenceRoom =
                conferenceRoomFactory.create(conferenceRoom);

            if (conferenceRoomService.checkIfExists(coreDomainConferenceRoom)) {
                return new ResponseEntity<>("Conference room already exists!", HttpStatus.CONFLICT);
            }
            conferenceRoomService.create(coreDomainConferenceRoom);

        } catch(ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return  new ResponseEntity<>("Conference room created.", HttpStatus.CREATED);

        //TODO: validate input params
        //TODO: return 'Invalid input format', bad request
        //TODO: get all conference rooms
        //TODO: check if conference room exists
        //TODO: return string 'Conference room already exists!'
        //TODO: create conference room
        //TODO: handle database exception: return 'Error creating conference room', internal server error
        //TODO: return 'Conference room created', created
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<AvailableConferenceRooms> getAvailableConferenceRooms(@RequestParam String conferenceStartDateTime,
                                                                                @RequestParam String conferenceEndDateTime) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/conference-room-availability", produces = "application/json")
    public ResponseEntity<ConferenceRoomAvailability> getConferenceRoomAvailability(@PathVariable int id) {
        return null;
    }
}
