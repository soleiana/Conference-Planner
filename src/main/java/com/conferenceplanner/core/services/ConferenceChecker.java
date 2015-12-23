package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConferenceChecker {

    public boolean isAvailable(Conference conference) {
        return conference.getConferenceRoomAvailabilityItems().stream()
                .anyMatch(ConferenceRoomAvailabilityItem::hasAvailableSeats);

    }

    public boolean compare(Conference conference, Conference conferenceToCompare) {
        return conferenceToCompare.getName().equalsIgnoreCase(conference.getName())
                && conferenceToCompare.getStartDateTime().equals(conference.getStartDateTime())
                && conferenceToCompare.getEndDateTime().equals(conference.getEndDateTime());

    }
}
