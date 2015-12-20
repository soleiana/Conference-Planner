package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConferenceRoomServiceAssistant {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    public boolean checkIfConferenceRoomExists(ConferenceRoom conferenceRoom) {
        try {
            List<ConferenceRoom> conferenceRooms = conferenceRoomRepository.getAll();

            for (ConferenceRoom room : conferenceRooms) {
                if (room.getName().equalsIgnoreCase(conferenceRoom.getName())
                        && room.getLocation().equalsIgnoreCase(conferenceRoom.getLocation())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return false;
    }

    public void createConferenceRoom(ConferenceRoom conferenceRoom) {
        try {
            conferenceRoomRepository.create(conferenceRoom);

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }
}
