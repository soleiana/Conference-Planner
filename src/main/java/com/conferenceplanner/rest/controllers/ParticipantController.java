package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.core.services.AccessException;
import com.conferenceplanner.core.services.ParticipantService;
import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.domain.ResourceAccessErrorCode;
import com.conferenceplanner.rest.validators.ParticipantValidator;
import com.conferenceplanner.rest.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/participants")
@Transactional
public class ParticipantController {

    @Autowired
    private ParticipantValidator participantValidator;

    @Autowired
    private ParticipantService participantService;

    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "application/json")
    public ResponseEntity<String> addParticipant(@RequestBody Participant participant) {
        return null;

    }

    @RequestMapping(method = RequestMethod.DELETE, consumes =  "application/json", produces = "application/json")
    public ResponseEntity<String> removeParticipant(@RequestBody Participant participant) {
        try {
            participantValidator.validateIds(participant);
            participantService.removeParticipant(participant.getId(), participant.getConferenceId());

        } catch (ValidationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (AccessException ex) {
            HttpStatus httpStatus = ResourceAccessErrorCode.getHttpStatus(ex.getErrorCode());
            return new ResponseEntity<>(ex.getMessage(), httpStatus);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>("Participant removed.", HttpStatus.OK);
    }


}
