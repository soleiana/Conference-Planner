package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/conferences")
public class ConferenceController {

    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "application/json" )
    public ResponseEntity<String> createConference(@RequestBody Conference conference) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
    public ResponseEntity<Conference> getConference(@PathVariable int id) {
        return null;
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Conference>> getConferences(@RequestParam boolean cancelled, @RequestParam boolean availableOnly) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/participants", produces = "application/json")
    public ResponseEntity<List<Participant>> getParticipants(@PathVariable int id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = "application/json")
    public ResponseEntity<String> cancelConference(@PathVariable int id) {
        return null;
    }

}
