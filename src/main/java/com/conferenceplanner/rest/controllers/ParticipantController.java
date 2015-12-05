package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/participants"
 , consumes =  "application/json", produces = "application/json")
public class ParticipantController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createParticipant(@RequestBody Participant participant) {
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<String> removeParticipant(@PathVariable int id) {
        return null;
    }

}
