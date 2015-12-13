package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConferenceChecker {

    public boolean isAvailable(Conference conference) {

        List<ConferenceRoomAvailabilityItem> availabilityItems = conference.getConferenceRoomAvailabilityItems();

        for (ConferenceRoomAvailabilityItem item: availabilityItems) {
            if (item.getAvailableSeats() > 0) {
                return true;
            }
        }

        return false;
    }
}
