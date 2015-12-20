package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailability;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Component
@Transactional
public class ConferenceRoomService {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Autowired
    private ConferenceRoomChecker conferenceRoomChecker;

    @Autowired
    private ConferenceRoomServiceAssistant serviceAssistant;


    public void createConferenceRoom(ConferenceRoom conferenceRoom) {
       if (serviceAssistant.checkIfConferenceRoomExists(conferenceRoom)) {
           throw new AccessException("Conference room already exists!", AccessErrorCode.CONFLICT);
       }
       serviceAssistant.createConferenceRoom(conferenceRoom);
    }


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
        if (availableRooms.isEmpty()) {
            throw new AccessException("No conference rooms found for selected conference interval!", AccessErrorCode.NOT_FOUND);
        }
        return availableRooms;
    }

    public boolean checkIfConferenceRoomsAvailable(List<Integer> conferenceRoomIds, Conference plannedConference) {
        List<ConferenceRoom> availableRooms = getAvailableConferenceRooms(plannedConference);

        List<Integer> availableRoomIds = availableRooms.stream()
                .map(ConferenceRoom::getId)
                .collect(Collectors.toList());

        return availableRoomIds.containsAll(conferenceRoomIds);
    }

    public ConferenceRoomAvailability getConferenceRoomAvailabilityItems(int conferenceRoomId) {
        ConferenceRoomAvailability availability = new ConferenceRoomAvailability();
        List<ConferenceRoomAvailabilityItem> availabilityItems;

        com.conferenceplanner.core.domain.ConferenceRoom conferenceRoom = serviceAssistant.getConferenceRoom(conferenceRoomId);
        if (conferenceRoom == null) {
            throw new AccessException("No conference room found for selected id!", AccessErrorCode.NOT_FOUND);
        }
        availabilityItems = serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom);
        if (availabilityItems.isEmpty()) {
            throw new AccessException("No upcoming conferences in this conference room!", AccessErrorCode.NOT_FOUND);
        }
        availability.setConferenceRoom(conferenceRoom);
        availability.setAvailabilityItems(availabilityItems);
        return availability;
    }

}
