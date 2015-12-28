package com.conferenceplanner.rest.controllers;


import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ConferenceService;
import com.conferenceplanner.rest.domain.*;
import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceParticipants;
import com.conferenceplanner.rest.factories.ConferenceFactory;
import com.conferenceplanner.rest.factories.ConferenceParticipantFactory;
import com.conferenceplanner.rest.validators.ConferenceValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = "/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private ConferenceValidator conferenceValidator;

    @Autowired
    private ConferenceFactory conferenceFactory;

    @Autowired
    private ConferenceParticipantFactory conferenceParticipantFactory;


    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "text/plain")
    public ResponseEntity<String> createConference(@RequestBody Conference conference) {
        try {
            conferenceValidator.validate(conference);
            com.conferenceplanner.core.domain.Conference coreDomainConference = conferenceFactory.create(conference);
            conferenceService.createConference(coreDomainConference, conference.getConferenceRoomIds());

        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (ApplicationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Conference created.", HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/upcoming", produces = "application/json")
    public ResponseEntity<Conferences> getUpcomingConferences() {
        Conferences conferences = new Conferences();
        try {
            List<com.conferenceplanner.core.domain.Conference> coreDomainConferences = conferenceService.getUpcomingConferences();
            List<Conference> restDomainConferences = conferenceFactory.create(coreDomainConferences);
            conferences.setConferences(restDomainConferences);

        } catch (ApplicationException ex) {
            conferences.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferences, HttpStatus.NOT_FOUND);

        } catch (RuntimeException ex) {
            conferences.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferences, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(conferences, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/available", produces = "application/json")
    public ResponseEntity<Conferences> getAvailableConferences() {
        Conferences conferences = new Conferences();
        try {
            List<com.conferenceplanner.core.domain.Conference> coreDomainConferences = conferenceService.getAvailableConferences();
            List<Conference> restDomainConferences = conferenceFactory.create(coreDomainConferences);
            conferences.setConferences(restDomainConferences);

        } catch (ApplicationException ex) {
            conferences.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferences, HttpStatus.NOT_FOUND);

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
            com.conferenceplanner.core.domain.ConferenceParticipants coreDomainConferenceParticipants = conferenceService.getParticipants(id);
            conferenceParticipants = conferenceParticipantFactory.create(coreDomainConferenceParticipants);

        } catch (ValidationException ex) {
            conferenceParticipants.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferenceParticipants, HttpStatus.BAD_REQUEST);

        } catch (ApplicationException ex) {
            conferenceParticipants.setErrorMessage(ex.getMessage());
            HttpStatus httpStatus = ResourceAccessErrorCode.getHttpStatus(ex.getErrorCode());
            return new ResponseEntity<>(conferenceParticipants, httpStatus);

        } catch (RuntimeException ex) {
            conferenceParticipants.setErrorMessage(ex.getMessage());
            return new ResponseEntity<>(conferenceParticipants, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(conferenceParticipants, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "text/plain")
    public ResponseEntity<String> cancelConference(@PathVariable Integer id) {
        try {
            conferenceValidator.validateId(id);
            conferenceService.cancelConference(id);

        } catch (ApplicationException ex) {
            HttpStatus httpStatus = ResourceAccessErrorCode.getHttpStatus(ex.getErrorCode());
            return new ResponseEntity<>(ex.getMessage(), httpStatus);
        }
        catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Conference cancelled.", HttpStatus.OK);
    }

}
