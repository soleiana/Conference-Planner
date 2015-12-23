package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class ConferenceRoomServiceAssistant {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRoomChecker conferenceRoomChecker;


    public boolean checkIfConferenceRoomExists(ConferenceRoom conferenceRoom) {
        try {
            List<ConferenceRoom> conferenceRooms = conferenceRoomRepository.getAll();
            return conferenceRooms.stream().anyMatch(room -> conferenceRoomChecker.compare(conferenceRoom, room));

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

    public void createConferenceRoom(ConferenceRoom conferenceRoom) {
        try {
            conferenceRoomRepository.create(conferenceRoom);

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

    public ConferenceRoom getConferenceRoom(Integer id) {
        try {
            return conferenceRoomRepository.getById(id);

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems(ConferenceRoom conferenceRoom) {
        try {
            return conferenceRoom.getConferenceRoomAvailabilityItems()
                .stream()
                .filter(item -> item.getConference().isUpcoming())
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

}
