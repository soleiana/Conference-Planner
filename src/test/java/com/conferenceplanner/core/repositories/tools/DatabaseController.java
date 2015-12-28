package com.conferenceplanner.core.repositories.tools;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ConferenceRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomAvailabilityItemRepository;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;


@Component
public class DatabaseController {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceRoomAvailabilityItemRepository conferenceRoomAvailabilityItemRepository;

    @Autowired
    private ParticipantRepository participantRepository;


    public void persistConferences(List<Conference> conferences) {
        conferences.stream().forEach(conferenceRepository::create);
    }

    public void persistConference(Conference conference) {
        conferenceRepository.create(conference);
    }

    public void persistConferenceRooms(List<ConferenceRoom> conferenceRooms) {
        conferenceRooms.stream().forEach(conferenceRoomRepository::create);
    }

    public void persistConferenceRoom(ConferenceRoom conferenceRoom) {
        conferenceRoomRepository.create(conferenceRoom);
    }

    public void persistParticipants(List<Participant> participants) {
       participants.stream().forEach(participantRepository::create);
    }

    public void persistParticipant(Participant participant) {
        participantRepository.create(participant);
    }


    public void setupRelationship(List<ConferenceRoom> conferenceRooms, List<Conference> conferences,
                                  List<ConferenceRoomAvailabilityItem> availabilityItems) {

        if (conferenceRooms.size() != availabilityItems.size()) {
            throw new IllegalArgumentException("Number of rooms should be equal to number of availability items!");
        }

        for (int i = 0; i < conferenceRooms.size(); i++) {
            for (Conference conference: conferences) {
                setupRelationship(conferenceRooms.get(i), conference, availabilityItems.get(i));
            }
        }
    }

    public void setupRelationship(List<ConferenceRoom> conferenceRooms, List<Conference> conferences) {
        conferenceRooms.stream()
                .forEach(room -> conferences.stream()
                        .forEach(conference -> setupRelationship(room, conference, new ConferenceRoomAvailabilityItem(room.getMaxSeats()))
                        )
                );
    }

    public void setupRelationship(ConferenceRoom conferenceRoom, List<Conference> conferences) {
        conferences.stream().forEach(conference -> setupRelationship(conferenceRoom, conference, new ConferenceRoomAvailabilityItem(conferenceRoom.getMaxSeats())));
    }

    public void setupRelationship(List<ConferenceRoom> conferenceRooms, Conference conference) {
        conferenceRooms.stream().forEach(room -> setupRelationship(room, conference, new ConferenceRoomAvailabilityItem(room.getMaxSeats())));
    }

    public void setupRelationship(ConferenceRoom conferenceRoom, Conference conference) {
        setupRelationship(conferenceRoom, conference, new ConferenceRoomAvailabilityItem(conferenceRoom.getMaxSeats()));
    }

    public void setupRelationship(Conference conference, List<Participant> participants) {
        participants.stream().forEach(participant ->  setupRelationship(conference, participant));
    }

    public void setupRelationship(Conference conference, Participant participant) {
        conference.addParticipant(participant);
    }

    public void setupRelationshipAndTakeAvailableSeat(Conference conference, Participant participant) {
        conference.addParticipant(participant);

        ConferenceRoomAvailabilityItem availabilityItem = conference.getConferenceRoomAvailabilityItems().stream()
                .filter(ConferenceRoomAvailabilityItem::hasAvailableSeats)
                .findFirst()
                .get();
        availabilityItem.takeAvailableSeat();
    }

    private void setupRelationship(ConferenceRoom conferenceRoom, Conference conference, ConferenceRoomAvailabilityItem availabilityItem) {
        conference.addConferenceRoomAvailabilityItem(availabilityItem);
        conferenceRoom.addConferenceRoomAvailabilityItem(availabilityItem);
        availabilityItem.setConferenceRoom(conferenceRoom);
        availabilityItem.setConference(conference);
        conferenceRoomAvailabilityItemRepository.create(availabilityItem);
    }
}
