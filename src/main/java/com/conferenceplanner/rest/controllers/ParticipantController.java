package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.Participant;
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

    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "application/json")
    public ResponseEntity<String> addParticipant(@RequestBody Participant participant) {
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = "application/json")
    public ResponseEntity<String> removeParticipant(@PathVariable int id) {
        return null;
    }

}
