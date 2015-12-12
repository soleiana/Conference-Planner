package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceInterval;
import com.conferenceplanner.core.domain.ConferenceRoom;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class RoomAvailabilityChecker {

    public boolean isAvailable(ConferenceRoom conferenceRoom, Conference plannedConference) {

        List<Conference> conferences = conferenceRoom.getConferences();
        if (conferences.isEmpty()) {
            return true;
        }
        for (Conference conference: conferences) {
            if (conference.isCancelled()){
                continue;
            }

            if (overlap(conference, plannedConference)) {
                return false;
            }
        }
        return true;
    }

    private boolean overlap(Conference scheduledConference, Conference plannedConference) {

        LocalDateTime plannedStart = plannedConference.getStartDateTime();
        LocalDateTime plannedEnd = plannedConference.getEndDateTime();
        LocalDateTime scheduledStart = scheduledConference.getStartDateTime();
        LocalDateTime scheduledEnd = scheduledConference.getEndDateTime();

        LocalDateTime actualScheduledEnd = ConferenceInterval.getActualEndDateTime(scheduledEnd);
        ConferenceInterval scheduledInterval = new ConferenceInterval(scheduledStart, actualScheduledEnd);
        ConferenceInterval plannedInterval = new ConferenceInterval(plannedStart, plannedEnd);

        if (plannedInterval.contains(scheduledStart)
                || plannedInterval.contains(actualScheduledEnd)
                || scheduledInterval.contains(plannedStart)
                || scheduledInterval.contains(plannedEnd)) {

            return false;
        }
    return true;
    }
}
