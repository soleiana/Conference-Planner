package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/conferences"
        , consumes =  "application/json", produces = "application/json")
public class ConferenceController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createConference(@RequestBody Conference conference) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Conference> getConference(@PathVariable int id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Conference>> getUpcomingConferences() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/available")
    public ResponseEntity<List<Conference>> getUpcomingAvailableConferences() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/participants")
    public ResponseEntity<List<Participant>> getParticipants(@PathVariable int id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<String> cancelConference(@PathVariable int id) {
        return null;
    }

}
