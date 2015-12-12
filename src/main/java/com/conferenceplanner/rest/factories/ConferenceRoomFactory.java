package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.rest.factories.helpers.StringNormalizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ConferenceRoomFactory {

    public ConferenceRoom create(com.conferenceplanner.rest.domain.ConferenceRoom conferenceRoom) {
        String name = StringNormalizer.createNormalizedConferenceRoomName(conferenceRoom.getName());
        String location = StringNormalizer.createNormalizedConferenceRoomLocation(conferenceRoom.getLocation());
        return new ConferenceRoom(name, location, conferenceRoom.getMaxSeats());
    }

    public List<com.conferenceplanner.rest.domain.ConferenceRoom> create(List<ConferenceRoom> conferenceRooms) {
        List<com.conferenceplanner.rest.domain.ConferenceRoom> rooms = new ArrayList<>();
        for (ConferenceRoom coreDomainRoom: conferenceRooms){
            com.conferenceplanner.rest.domain.ConferenceRoom room = create(coreDomainRoom);
            rooms.add(room);
        }
        return rooms;
    }

    private com.conferenceplanner.rest.domain.ConferenceRoom create(ConferenceRoom conferenceRoom) {
        com.conferenceplanner.rest.domain.ConferenceRoom room = new com.conferenceplanner.rest.domain.ConferenceRoom();
        room.setId(conferenceRoom.getId());
        room.setLocation(conferenceRoom.getLocation());
        room.setName(conferenceRoom.getName());
        room.setMaxSeats(conferenceRoom.getMaxSeats());
        return room;
    }
}
