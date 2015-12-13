package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.rest.domain.*;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.ConferenceRoomAvailability;
import com.conferenceplanner.rest.factories.ConferenceFactory;
import com.conferenceplanner.rest.factories.ConferenceRoomAvailabilityFactory;
import com.conferenceplanner.rest.factories.ConferenceRoomFactory;
import com.conferenceplanner.rest.validators.ConferenceRoomValidator;
import com.conferenceplanner.rest.validators.ConferenceValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = "/conference-rooms")
public class ConferenceRoomController {

    @Autowired
    private ConferenceRoomValidator conferenceRoomValidator;

    @Autowired
    private ConferenceValidator conferenceValidator;

    @Autowired
    private ConferenceRoomFactory conferenceRoomFactory;

    @Autowired
    private ConferenceRoomAvailabilityFactory conferenceRoomAvailabilityFactory;

    @Autowired
    private ConferenceFactory conferenceFactory;

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
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<AvailableConferenceRooms> getAvailableConferenceRooms(@RequestParam String conferenceStartDateTime,
                                                                                @RequestParam String conferenceEndDateTime) {
        AvailableConferenceRooms availableConferenceRooms = new AvailableConferenceRooms();

        try {
            ConferenceInterval interval = conferenceValidator.validate(conferenceStartDateTime, conferenceEndDateTime);

            com.conferenceplanner.core.domain.Conference coreDomainConference =
                    conferenceFactory.create(interval);

            List<com.conferenceplanner.core.domain.ConferenceRoom> coreDomainConferenceRooms =
                    conferenceRoomService.getAvailableConferenceRooms(coreDomainConference);

            if (coreDomainConferenceRooms.isEmpty()) {
                availableConferenceRooms.setErrorMessage("No conference rooms found for selected conference interval!");
                return new ResponseEntity<>(availableConferenceRooms, HttpStatus.NOT_FOUND);
            }

            List<ConferenceRoom> conferenceRooms = conferenceRoomFactory.create(coreDomainConferenceRooms);
            availableConferenceRooms.setAvailableConferenceRooms(conferenceRooms);

            availableConferenceRooms.setConferenceStartDateTime(interval.getFormattedStartDateTime());
            availableConferenceRooms.setConferenceEndDateTime(interval.getFormattedEndDateTime());

        } catch(ValidationException ex) {
            availableConferenceRooms.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(availableConferenceRooms, HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            availableConferenceRooms.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(availableConferenceRooms, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return  new ResponseEntity<>(availableConferenceRooms, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/conference-room-availability", produces = "application/json")
    public ResponseEntity<ConferenceRoomAvailability> getConferenceRoomAvailability(@PathVariable Integer id) {

        ConferenceRoomAvailability conferenceRoomAvailability = new ConferenceRoomAvailability();

        try {
            conferenceRoomValidator.validate(id);

            com.conferenceplanner.core.domain.ConferenceRoom coreDomainConferenceRoom =
                    conferenceRoomService.getConferenceRoom(id);

            if (coreDomainConferenceRoom == null) {
                conferenceRoomAvailability.setErrorMessage("No conference room found for selected id!");
                return new ResponseEntity<>(conferenceRoomAvailability, HttpStatus.NOT_FOUND);
            }

            List<com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem> coreDomainConferenceRoomAvailabilityItems =
                    conferenceRoomService.getConferenceRoomAvailabilityItems(coreDomainConferenceRoom);

            if (coreDomainConferenceRoomAvailabilityItems.isEmpty()) {
                conferenceRoomAvailability.setErrorMessage("No upcoming conferences in this conference room!");
                return new ResponseEntity<>(conferenceRoomAvailability, HttpStatus.NOT_FOUND);
            }

            conferenceRoomAvailability = conferenceRoomAvailabilityFactory.create(coreDomainConferenceRoomAvailabilityItems, coreDomainConferenceRoom);

        } catch(ValidationException ex) {
            conferenceRoomAvailability.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferenceRoomAvailability, HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            conferenceRoomAvailability.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferenceRoomAvailability, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>(conferenceRoomAvailability, HttpStatus.OK);

        //TODO: validate id
        //TODO: get conference room with specified id
        //TODO: if conference room not found, return NOT_FOUND, "No conference room found for selected id!"
        //TODO: get conference room availabilities for conference room
        //TODO: if no conference room availabilities found, return NOT_FOUND, "No conferences scheduled in this conference room"
        //TODO: get conference for each conference room availability
        //TODO: check if conference is upcoming (startDateTime after now, not cancelled)
        //TODO: add specified conference room  availability to the list
        //TODO: if the list is empty, return NOT_FOUND, "No conferences scheduled in this conference room"
        //TODO: build ConferenceRoomAvailabilityItem rest model based on this list, conference room and conference
        //TODO: handle database exception: return internal server error
        //TODO: return OK and ConferenceRoomAvailabilityItem
    }
}
