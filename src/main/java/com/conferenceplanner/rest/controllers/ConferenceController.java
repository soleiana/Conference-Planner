package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.services.ConferenceRoomService;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.rest.domain.*;
import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.factories.ConferenceFactory;
import com.conferenceplanner.rest.factories.ConferenceParticipantFactory;
import com.conferenceplanner.rest.validators.ConferenceValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/conferences")
@Transactional
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ConferenceValidator conferenceValidator;

    @Autowired
    private ConferenceFactory conferenceFactory;

    @Autowired
    private ConferenceParticipantFactory conferenceParticipantFactory;


    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "application/json" )
    public ResponseEntity<String> createConference(@RequestBody Conference conference) {
        try {
            conferenceValidator.validate(conference);
            com.conferenceplanner.core.domain.Conference coreDomainConference = conferenceFactory.create(conference);

            if(conferenceService.checkIfConferenceExists(coreDomainConference)) {
                return new ResponseEntity<>("Conference already exists!", HttpStatus.CONFLICT);
            }

            if(conferenceRoomService.checkIfConferenceRoomsAvailable(conference.getConferenceRoomIds(), coreDomainConference)) {
                return new ResponseEntity<>("Conference room(s) not available!", HttpStatus.CONFLICT);
            }

            conferenceService.createConference(coreDomainConference);
            conferenceRoomService.registerConference(coreDomainConference, conference.getConferenceRoomIds());

        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>("Conference created.", HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{upcoming}", produces = "application/json")
    public ResponseEntity<Conferences> getUpcomingConferences(@PathVariable String upcoming) {
        Conferences conferences = new Conferences();
        try {
            List<com.conferenceplanner.core.domain.Conference> coreDomainConferences = conferenceService.getUpcomingConferences();

            if (coreDomainConferences.isEmpty()) {
                conferences.setErrorMessage("No conferences available for cancellation!");
                return new ResponseEntity<>(conferences, HttpStatus.NOT_FOUND);
            }

        } catch (RuntimeException ex) {
            conferences.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferences, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(conferences, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{available}", produces = "application/json")
    public ResponseEntity<Conferences> getAvailableConferences(@PathVariable String available) {
        Conferences conferences = new Conferences();
        try {
            List<com.conferenceplanner.core.domain.Conference> coreDomainConferences = conferenceService.getAvailableConferences();

            if (coreDomainConferences.isEmpty()) {
                conferences.setErrorMessage("No conferences available for registration!");
                return new ResponseEntity<>(conferences, HttpStatus.NOT_FOUND);
            }

        } catch (RuntimeException ex) {
            conferences.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferences, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(conferences, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}/participants", produces = "application/json")
    public ResponseEntity<ConferenceParticipants> getParticipants(@PathVariable Integer id) {

        ConferenceParticipants conferenceParticipants = new ConferenceParticipants();
        try {
            conferenceValidator.validateId(id);
            com.conferenceplanner.core.domain.Conference coreDomainConference = conferenceService.getConference(id);

            if (coreDomainConference == null) {
                conferenceParticipants.setErrorMessage("No conference found for selected id!");
                return new ResponseEntity<>(conferenceParticipants, HttpStatus.NOT_FOUND);
            }
            if (!coreDomainConference.isUpcoming()) {
                conferenceParticipants.setErrorMessage("Conference is not upcoming!");
                return new ResponseEntity<>(conferenceParticipants, HttpStatus.CONFLICT);
            }
            List<com.conferenceplanner.core.domain.Participant> coreDomainParticipants = participantService.getParticipants(coreDomainConference);
            if (coreDomainParticipants.isEmpty()) {
                conferenceParticipants.setErrorMessage("No participants found for selected conference");
                return new ResponseEntity<>(conferenceParticipants, HttpStatus.NOT_FOUND);
            }
            conferenceParticipants = conferenceParticipantFactory.create(coreDomainConference, coreDomainParticipants);

        } catch (ValidationException ex) {
            conferenceParticipants.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferenceParticipants, HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            conferenceParticipants.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferenceParticipants, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(conferenceParticipants, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    public ResponseEntity<String> cancelConference(@PathVariable Integer id) {

        try {
            conferenceValidator.validateId(id);
            com.conferenceplanner.core.domain.Conference coreDomainConference = conferenceService.getConference(id);

            if (coreDomainConference == null) {
                return new ResponseEntity<>("No conference found for selected id!", HttpStatus.NOT_FOUND);
            }
            if (coreDomainConference.isCancelled()) {
                return new ResponseEntity<>("Conference already cancelled", HttpStatus.CONFLICT);
            }
            conferenceService.cancelConference(coreDomainConference);

        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Conference cancelled.", HttpStatus.OK);

    }

}
