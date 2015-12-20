package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConferenceRoomServiceAssistant {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemChecker conferenceRoomAvailabilityItemChecker;

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

    public ConferenceRoom getConferenceRoom(Integer id) {
        ConferenceRoom conferenceRoom;
        try {
            conferenceRoom = conferenceRoomRepository.getById(id);

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return conferenceRoom;
    }

    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems(ConferenceRoom conferenceRoom) {
        try {
            return conferenceRoom.getConferenceRoomAvailabilityItems()
                .stream()
                .filter(conferenceRoomAvailabilityItemChecker::isActual)
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

}
