package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ConferenceRoomService {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Autowired
    private ConferenceRoomChecker conferenceRoomChecker;

    @Autowired
    private ConferenceRoomAvailabilityItemChecker conferenceRoomAvailabilityItemChecker;


    @Transactional
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

    @Transactional
    public void createConferenceRoom(ConferenceRoom conferenceRoom) {
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

        List<ConferenceRoom> availableRooms;
        try {
            List<ConferenceRoom> allRooms = conferenceRoomRepository.getAll();
            availableRooms = allRooms.stream()
                    .filter(room -> conferenceRoomChecker.isAvailable(room, plannedConference))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }

        return availableRooms;
    }

    @Transactional
    public boolean checkIfConferenceRoomsAvailable(List<Integer> conferenceRoomIds, Conference plannedConference) {
        List<ConferenceRoom> availableRooms = getAvailableConferenceRooms(plannedConference);

        List<Integer> availableRoomIds = availableRooms.stream()
                .map(ConferenceRoom::getId)
                .collect(Collectors.toList());

        return availableRoomIds.containsAll(conferenceRoomIds);
    }

    @Transactional
    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems(ConferenceRoom conferenceRoom) {

        List<ConferenceRoomAvailabilityItem> actualAvailabilityItems;
        try {
            List<ConferenceRoomAvailabilityItem> allAvailabilityItems = conferenceRoom.getConferenceRoomAvailabilityItems();
            actualAvailabilityItems = allAvailabilityItems.stream()
                    .filter(conferenceRoomAvailabilityItemChecker::isActual)
                    .collect(Collectors.toList());
        }  catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }

        return actualAvailabilityItems;
    }

    @Transactional
    public void registerConference(Conference conference, List<Integer> conferenceRoomIds) {
        try {
            for (int roomId : conferenceRoomIds) {
                ConferenceRoom room = conferenceRoomRepository.getById(roomId);
                ConferenceRoomAvailabilityItem availabilityItem = new ConferenceRoomAvailabilityItem(room.getMaxSeats());
                availabilityItem.setConference(conference);
                availabilityItem.setConferenceRoom(room);
                room.addConferenceRoomAvailabilityItem(availabilityItem);
                conference.addConferenceRoomAvailabilityItem(availabilityItem);
                conferenceRoomAvailabilityItemRepository.create(availabilityItem);
                room.addConference(conference);
            }
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }
}
