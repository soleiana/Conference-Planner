package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/conference-rooms")
public class ConferenceRoomController {

    @RequestMapping(method = RequestMethod.POST, consumes =  "application/json", produces = "application/json")
    public ResponseEntity<String> createConferenceRoom(@RequestBody ConferenceRoom conferenceRoom) {
        return null;
    }

    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<AvailableConferenceRooms> getAvailableConferenceRooms(@RequestParam String conferenceStartDateTime,
                                                                                @RequestParam String conferenceEndDateTime) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/conference-room-availability", produces = "application/json")
    public ResponseEntity<ConferenceRoomAvailability> getConferenceRoomAvailability(@PathVariable int id) {
        return null;
    }
}
