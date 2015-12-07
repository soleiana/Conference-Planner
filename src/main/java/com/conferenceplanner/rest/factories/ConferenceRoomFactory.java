package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.rest.factories.helpers.StringNormalizer;
import org.springframework.stereotype.Component;


@Component
public class ConferenceRoomFactory {

    public ConferenceRoom create(com.conferenceplanner.rest.domain.ConferenceRoom conferenceRoom) {

        String name = StringNormalizer.createNormalizedConferenceRoomName(conferenceRoom.getName());
        String location = StringNormalizer.createNormalizedConferenceRoomLocation(conferenceRoom.getLocation());
        return new ConferenceRoom(name, location, conferenceRoom.getMaxSeats());
    }
}
