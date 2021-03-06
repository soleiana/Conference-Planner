package com.conferenceplanner.rest.validators;

import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.parsers.ParserException;
import com.conferenceplanner.rest.parsers.ConferenceRoomParser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConferenceRoomValidator {

    private static final int MIN_SEATS_IN_CONFERENCE_ROOM = 5;
    private static final int MAX_SEATS_IN_CONFERENCE_ROOM = 2000;

    public void validate(ConferenceRoom conferenceRoom) {
        validateMaxSeats(conferenceRoom.getMaxSeats());
        validateNames(conferenceRoom.getLocation(), conferenceRoom.getName());
    }

    public void validateIds(List<Integer> conferenceRoomIds) {
        if (conferenceRoomIds == null || conferenceRoomIds.isEmpty()) {
            throw new ValidationException("Conference room id list is null or empty");
        }
    }

    private void validateNames(String locationString, String nameString) {
        if (nameString == null || locationString == null) {
            throw new ValidationException("Conference room name or location is null");
        }
        try {
            ConferenceRoomParser.parse(locationString, nameString);

        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    private void validateMaxSeats(Integer maxSeats) {
        if (maxSeats == null) {
            throw new ValidationException("Conference room max seats number is null");
        }
        if (maxSeats < MIN_SEATS_IN_CONFERENCE_ROOM || maxSeats > MAX_SEATS_IN_CONFERENCE_ROOM) {
            throw new ValidationException("Invalid max seats");
        }
    }

}
