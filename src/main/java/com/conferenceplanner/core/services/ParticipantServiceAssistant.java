package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.domain.Participant;
import com.conferenceplanner.core.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class ParticipantServiceAssistant {

    @Autowired
    private ParticipantRepository participantRepository;


    public Participant getParticipant(int participantId) {
        return participantRepository.getById(participantId);
    }

    public void removeParticipant(Participant participant, Conference conference) {
        conference.removeParticipant(participant);

        ConferenceRoomAvailabilityItem availabilityItem = conference.getConferenceRoomAvailabilityItems().stream()
                .filter(item -> !item.relatesToEmptyConferenceRoom())
                .findFirst()
                .get();
        availabilityItem.releaseAvailableSeat();
    }
}
