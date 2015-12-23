package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import com.conferenceplanner.core.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ParticipantServiceAssistant {

    @Autowired
    private ParticipantRepository participantRepository;


    public Participant getParticipant(int participantId) {
        try {
            return participantRepository.getById(participantId);
        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }

    public void removeParticipant(Participant participant, Conference conference) {
        try {
            conference.removeParticipant(participant);

            ConferenceRoomAvailabilityItem availabilityItem = conference.getConferenceRoomAvailabilityItems().stream()
                    .filter(item -> !item.relatesToEmptyConferenceRoom())
                    .findFirst()
                    .get();
            availabilityItem.releaseAvailableSeat();

        } catch (Exception ex) {
            throw new DatabaseException("Persistence level error: " + ex.getMessage());
        }
    }
}
