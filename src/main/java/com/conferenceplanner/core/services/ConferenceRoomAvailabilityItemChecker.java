package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.stereotype.Component;

@Component
public class ConferenceRoomAvailabilityItemChecker {

    public boolean isActual(ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem) {
        Conference conference = conferenceRoomAvailabilityItem.getConference();
        if (conference.isUpcoming()) {
            return true;
        }
        return false;
    }
}
