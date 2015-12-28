package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
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
        return conferenceRoomRepository.getAll().stream().
                    anyMatch(room -> conferenceRoomChecker.compare(conferenceRoom, room));
    }

    public void createConferenceRoom(ConferenceRoom conferenceRoom) {
        conferenceRoomRepository.create(conferenceRoom);
    }

    public ConferenceRoom getConferenceRoom(Integer id) {
        return conferenceRoomRepository.getById(id);
    }

    public List<ConferenceRoom> getConferenceRooms() {
        return conferenceRoomRepository.getAll().stream()
                .collect(Collectors.toList());
    }

    public List<ConferenceRoom> getAvailableConferenceRooms(Conference plannedConference) {
        return conferenceRoomRepository.getAll().stream()
                .filter(room -> conferenceRoomChecker.isAvailable(room, plannedConference))
                .collect(Collectors.toList());
    }

    public List<ConferenceRoomAvailabilityItem> getConferenceRoomAvailabilityItems(ConferenceRoom conferenceRoom) {
        return conferenceRoom.getConferenceRoomAvailabilityItems().stream()
                .filter(item -> item.getConference().isUpcoming())
                .collect(Collectors.toList());
    }

}
