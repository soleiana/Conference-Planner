package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailability;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Component
@Transactional
public class ConferenceRoomService {

    @Autowired
    private ConferenceRoomServiceAssistant serviceAssistant;


    public void createConferenceRoom(ConferenceRoom conferenceRoom) {
       if (serviceAssistant.checkIfConferenceRoomExists(conferenceRoom)) {
           throw new ApplicationException("Conference room already exists!", ApplicationErrorCode.CONFLICT);
       }
       serviceAssistant.createConferenceRoom(conferenceRoom);
    }

    public List<ConferenceRoom> getConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = serviceAssistant.getConferenceRooms();
        if (conferenceRooms.isEmpty()) {
            throw new ApplicationException("No conference rooms found!", ApplicationErrorCode.NOT_FOUND);
        }
        return conferenceRooms;
    }


    public List<ConferenceRoom> getAvailableConferenceRooms(Conference plannedConference) {
        List<ConferenceRoom> availableRooms = serviceAssistant.getAvailableConferenceRooms(plannedConference);
        if (availableRooms.isEmpty()) {
            throw new ApplicationException("No conference rooms found for selected conference interval!", ApplicationErrorCode.NOT_FOUND);
        }
        return availableRooms;
    }

    public boolean checkIfConferenceRoomsAreAvailable(List<Integer> conferenceRoomIds, Conference plannedConference) {
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
            throw new ApplicationException("No conference room found for selected id!", ApplicationErrorCode.NOT_FOUND);
        }
        availabilityItems = serviceAssistant.getConferenceRoomAvailabilityItems(conferenceRoom);
        if (availabilityItems.isEmpty()) {
            throw new ApplicationException("No upcoming conferences in this conference room!", ApplicationErrorCode.NOT_FOUND);
        }
        availability.setConferenceRoom(conferenceRoom);
        availability.setAvailabilityItems(availabilityItems);
        return availability;
    }

}
