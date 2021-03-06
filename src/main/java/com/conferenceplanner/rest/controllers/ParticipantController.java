package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.services.ApplicationException;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.domain.ResourceAccessErrorCode;
import com.conferenceplanner.rest.factories.ParticipantFactory;
import com.conferenceplanner.rest.validators.ParticipantValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/participants")
public class ParticipantController {

    @Autowired
    private ParticipantValidator participantValidator;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantFactory participantFactory;


    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "text/plain")
    public ResponseEntity<String> addParticipant(@RequestBody Participant participant) {
        try {
            participantValidator.validate(participant);
            com.conferenceplanner.core.domain.Participant coreDomainParticipant = participantFactory.create(participant);
            participantService.addParticipant(coreDomainParticipant, participant.getConferenceId());

        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (ApplicationException ex) {
            HttpStatus httpStatus = ResourceAccessErrorCode.getHttpStatus(ex.getErrorCode());
            return new ResponseEntity<>(ex.getMessage(), httpStatus);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>("Participant added.", HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.DELETE, consumes =  "application/json", produces = "text/plain")
    public ResponseEntity<String> removeParticipant(@RequestBody Participant participant) {
        try {
            participantValidator.validateIds(participant);
            participantService.removeParticipant(participant.getId(), participant.getConferenceId());

        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (ApplicationException ex) {
            HttpStatus httpStatus = ResourceAccessErrorCode.getHttpStatus(ex.getErrorCode());
            return new ResponseEntity<>(ex.getMessage(), httpStatus);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>("Participant removed.", HttpStatus.OK);
    }
}
