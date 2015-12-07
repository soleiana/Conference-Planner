package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/conference-rooms"
        , consumes =  "application/json", produces = "application/json")
public class ConferenceRoomController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createConferenceRoom(@RequestBody ConferenceRoom conferenceRoom) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ConferenceRoom> getConferenceRoom(@PathVariable int id) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/conference-interval")
    public ResponseEntity<AvailableConferenceRooms> getAvailableConferenceRooms(@RequestParam String startDateTime, @RequestParam String endDateTime) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/schedule")
    public ResponseEntity<ConferenceRoomAvailability> getConferenceRoomAvailability(@PathVariable int id) {
        return null;
    }
}
