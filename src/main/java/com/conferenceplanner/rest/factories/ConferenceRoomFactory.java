package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.rest.factories.helpers.StringNormalizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ConferenceRoomFactory {

    public ConferenceRoom create(com.conferenceplanner.rest.domain.ConferenceRoom conferenceRoom) {
        String name = StringNormalizer.createNormalizedConferenceRoomName(conferenceRoom.getName());
        String location = StringNormalizer.createNormalizedConferenceRoomLocation(conferenceRoom.getLocation());
        return new ConferenceRoom(name, location, conferenceRoom.getMaxSeats());
    }

    public List<com.conferenceplanner.rest.domain.ConferenceRoom> create(List<ConferenceRoom> conferenceRooms) {
        return conferenceRooms.stream().map(this::create).collect(Collectors.toList());
    }

    public com.conferenceplanner.rest.domain.ConferenceRoom create(ConferenceRoom conferenceRoom) {
        com.conferenceplanner.rest.domain.ConferenceRoom restDomainConferenceRoom = new com.conferenceplanner.rest.domain.ConferenceRoom();
        restDomainConferenceRoom.setId(conferenceRoom.getId());
        restDomainConferenceRoom.setLocation(conferenceRoom.getLocation());
        restDomainConferenceRoom.setName(conferenceRoom.getName());
        restDomainConferenceRoom.setMaxSeats(conferenceRoom.getMaxSeats());
        return restDomainConferenceRoom;
    }
}
