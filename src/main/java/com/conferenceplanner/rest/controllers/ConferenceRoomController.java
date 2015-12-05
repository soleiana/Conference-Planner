package com.conferenceplanner.rest.controllers;

import com.conferenceplanner.rest.domain.ConferenceInterval;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.ConferenceRoomSchedule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/rooms"
        , consumes =  "application/json", produces = "application/json")
public class ConferenceRoomController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createConferenceRoom(@RequestBody ConferenceRoom conferenceRoom) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ConferenceRoom>> getAvailableConferenceRooms(@RequestBody ConferenceInterval conferenceInterval) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/schedule")
    public ResponseEntity<ConferenceRoomSchedule> getConferenceRoomSchedule(@PathVariable int id) {
        return null;
    }
}
