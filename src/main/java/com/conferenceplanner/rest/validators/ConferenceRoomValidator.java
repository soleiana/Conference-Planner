package com.conferenceplanner.rest.validators;

import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.parsers.ParserException;
import com.conferenceplanner.rest.parsers.ConferenceRoomParser;
import org.springframework.stereotype.Component;

@Component
public class ConferenceRoomValidator {

    private static final int MIN_SEATS_IN_CONFERENCE_ROOM = 5;
    private static final int MAX_SEATS_IN_CONFERENCE_ROOM = 2000;

    public boolean validate(ConferenceRoom conferenceRoom) {

        String nameString = conferenceRoom.getName();
        String locationString = conferenceRoom.getLocation();
        Integer maxSeats = conferenceRoom.getMaxSeats();

        if (nameString == null || nameString.isEmpty()
                || locationString == null || locationString.isEmpty()
                || maxSeats == null) {
            throw new ValidationException("One ore more parameters are null or empty");
        }

        if (maxSeats < MIN_SEATS_IN_CONFERENCE_ROOM || maxSeats > MAX_SEATS_IN_CONFERENCE_ROOM) {
            throw new ValidationException("Invalid max seats");
        }
        try {
           // String locationStringToParse = locationString.toLowerCase().trim();
          //  String nameStringToParse = nameString.toLowerCase().trim();
            ConferenceRoomParser.parse(locationString, nameString);

        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
        return true;
    }

}
