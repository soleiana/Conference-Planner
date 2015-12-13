package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class ConferenceRoomService {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomChecker conferenceRoomChecker;

    @Autowired
    private ConferenceRoomAvailabilityItemChecker conferenceRoomAvailabilityItemChecker;


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
    public ConferenceRoom getConferenceRoom(Integer id) {
        ConferenceRoom conferenceRoom;
        try {
            conferenceRoom = conferenceRoomRepository.getById(id);

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return conferenceRoom;
    }

    @Transactional
    public List<ConferenceRoom> getAvailableConferenceRooms(Conference plannedConference) {

        List<ConferenceRoom> availableRooms = new ArrayList<>();
        try {
            List<ConferenceRoom> allRooms = conferenceRoomRepository.getAll();

            for (ConferenceRoom room: allRooms) {
                if (conferenceRoomChecker.isAvailable(room, plannedConference)) {
                    availableRooms.add(room);
                }
            }
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }

        return availableRooms;
    }

    @Transactional
    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems(ConferenceRoom conferenceRoom) {

        List<ConferenceRoomAvailabilityItem> actualAvailabilityItems = new ArrayList<>();
        try {
            List<ConferenceRoomAvailabilityItem> allAvailabilityItems = conferenceRoom.getConferenceRoomAvailabilityItems();
            for(ConferenceRoomAvailabilityItem availabilityItem: allAvailabilityItems) {

                if (conferenceRoomAvailabilityItemChecker.isActual(availabilityItem)) {
                    actualAvailabilityItems.add(availabilityItem);
                }
            }
        }  catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }

        return actualAvailabilityItems;
    }
}
