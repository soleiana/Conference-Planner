package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Component
@Transactional
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
        return conferenceRepository.getUpcoming().stream()
                .anyMatch(c -> conferenceChecker.compare(c, conference));
    }

    public void createConference(Conference conference) {
            conferenceRepository.create(conference);
    }

    public void registerConference(Conference conference, List<Integer> conferenceRoomIds) {
      conferenceRoomIds.stream().
              forEach(roomId -> registerConference(conference, roomId));
    }

    public void cancelConference(Conference conference) {
        conference.setCancelled(true);
    }

    public Conference getConference(int conferenceId) {
        return conferenceRepository.getById(conferenceId);
    }

    public List<Conference> getUpcomingConferences() {
        return conferenceRepository.getUpcoming();
    }

    public List<Conference> getAvailableConferences() {
        return conferenceRepository.getUpcoming().stream()
                .filter(conferenceChecker::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Participant> getParticipants(Conference conference) {
            return conference.getParticipants();
    }

    private void registerConference(Conference conference, int conferenceRoomId) {
        ConferenceRoom room = conferenceRoomRepository.getById(conferenceRoomId);
        ConferenceRoomAvailabilityItem availabilityItem = new ConferenceRoomAvailabilityItem(room.getMaxSeats());
        availabilityItem.setConference(conference);
        availabilityItem.setConferenceRoom(room);
        conference.addConferenceRoomAvailabilityItem(availabilityItem);
        conferenceRoomAvailabilityItemRepository.create(availabilityItem);
        room.addConference(conference);
    }

}
