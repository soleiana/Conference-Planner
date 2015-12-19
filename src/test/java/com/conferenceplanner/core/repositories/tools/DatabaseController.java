package com.conferenceplanner.core.repositories.tools;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Stack;

@Component
public class DatabaseController {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;


    @Transactional
    public void persistConferences(List<Conference> conferences) {
        for (Conference conference: conferences) {
            conferenceRepository.create(conference);
        }
    }

    @Transactional
    public void persistConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        for (ConferenceRoom room: conferenceRooms) {
            conferenceRoomRepository.create(room);
        }
    }

    @Transactional
    public void setupRelationship(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        for(ConferenceRoom room: conferenceRooms) {
            for (Conference conference: conferences)
                setupRelationship(room, conference);
        }
    }

    @Transactional
    public void setupRelationship(List<ConferenceRoom> conferenceRooms, List<Conference> conferences,
                                  List<ConferenceRoomAvailabilityItem> availabilityItems) {

        if (conferenceRooms.size() != availabilityItems.size()) {
            throw new IllegalArgumentException("Number of rooms should be equal to number of availability items!");
        }

        for(Conference conference: conferences) {
            Stack<ConferenceRoomAvailabilityItem> availabilityItemStack = new Stack<>();
            availabilityItemStack.addAll(availabilityItems);

            for (ConferenceRoom room: conferenceRooms) {
                ConferenceRoomAvailabilityItem availabilityItem = availabilityItemStack.pop();
                setupRelationship(room, conference, availabilityItem);
            }
        }
    }

    @Transactional
    public void setupRelationshipWithAvailability(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        for(ConferenceRoom room: conferenceRooms) {
            for (Conference conference: conferences)
                setupRelationship(room, conference, new ConferenceRoomAvailabilityItem(room.getMaxSeats()));
        }
    }

    private void setupRelationship(ConferenceRoom conferenceRoom, Conference conference) {
        conferenceRoom.getConferences().add(conference);
    }

    private void setupRelationship(ConferenceRoom conferenceRoom, Conference conference,
                                   ConferenceRoomAvailabilityItem availabilityItem) {

        conferenceRoom.addConferenceRoomAvailabilityItem(availabilityItem);
        conference.addConferenceRoomAvailabilityItem(availabilityItem);
        availabilityItem.setConferenceRoom(conferenceRoom);
        availabilityItem.setConference(conference);
        conferenceRoomAvailabilityItemRepository.create(availabilityItem);
    }
}
