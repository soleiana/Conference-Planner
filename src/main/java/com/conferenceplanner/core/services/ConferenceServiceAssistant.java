package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ConferenceServiceAssistant {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceChecker conferenceChecker;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;


    public boolean checkIfConferenceExists(Conference conference) {
        try {
            List<Conference> conferences = conferenceRepository.getUpcoming();

            for (Conference c : conferences) {
                if (c.getName().equalsIgnoreCase(conference.getName())
                        && c.getStartDateTime().equals(conference.getStartDateTime())
                        && c.getEndDateTime().equals(conference.getEndDateTime())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
        return false;
    }

    public void createConference(Conference conference) {
        try {
            conferenceRepository.create(conference);
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

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

    public List<Conference> getUpcomingConferences() {
        try {
            return conferenceRepository.getUpcoming();
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

    public List<Conference> getAvailableConferences() {
        try {
            return conferenceRepository.getUpcoming().stream()
                    .filter(conferenceChecker::isAvailable)
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }

    }

}
