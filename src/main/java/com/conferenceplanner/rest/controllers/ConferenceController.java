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

        //TODO: validate Conference (name, startDateTime, endDateTime, conference room ids)
        //TODO: check if a conference with a specified name, start and end dateTime exists
        //TODO: if conference exists, return CONFLICT, "Conference already exists!"
        //TODO: get conference rooms by id (request body)
        //TODO: check if all the selected conference rooms are available for planned conference
        //TODO: if not, return CONFLICT, "Conference room(s) already booked!"
        //TODO: create conference
        //TODO: add conference to each conference room
        //TODO: create conference room availability item for each conference room
        //TODO: set item.availableSeats = conferenceRoom.maxSeats
        //TODO: associate this item with the conference and conference room
        //TODO: handle validation exception: return BAD_REQUEST
        //TODO: handle database exception: return internal server error
        //TODO: return CREATED
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
