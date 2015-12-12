package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceInterval;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class ConferenceRoomService {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private RoomAvailabilityChecker roomAvailabilityChecker;


    @Transactional
    public boolean checkIfExists(ConferenceRoom conferenceRoom) {
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

    @Transactional
    public void create(ConferenceRoom conferenceRoom) {
        try {
            conferenceRoomRepository.create(conferenceRoom);

        } catch (Exception ex) {

            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

    @Transactional
    public List<ConferenceRoom> getAvailable(Conference plannedConference) {

        List<ConferenceRoom> availableRooms = new ArrayList<>();
        List<ConferenceRoom> allRooms = conferenceRoomRepository.getAll();

        for (ConferenceRoom room: allRooms) {
            if (roomAvailabilityChecker.isAvailable(room, plannedConference)) {
                    availableRooms.add(room);
            }
        }
        return availableRooms;
    }
}
