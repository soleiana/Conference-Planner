package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceInterval;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class ConferenceRoomChecker {

    public boolean isAvailable(ConferenceRoom conferenceRoom, Conference plannedConference) {
        return !conferenceRoom.getConferenceRoomAvailabilityItems().stream()
                .map(ConferenceRoomAvailabilityItem::getConference)
                .anyMatch(c -> !c.isCancelled() && this.overlap(c, plannedConference));
    }

    public boolean compare(ConferenceRoom conferenceRoom, ConferenceRoom roomToCompare) {
        return conferenceRoom.getName().equalsIgnoreCase(roomToCompare.getName())
                && conferenceRoom.getLocation().equalsIgnoreCase(roomToCompare.getLocation());
    }

    private boolean overlap(Conference scheduledConference, Conference plannedConference) {
        LocalDateTime plannedStart = plannedConference.getStartDateTime();
        LocalDateTime plannedEnd = plannedConference.getEndDateTime();
        LocalDateTime scheduledStart = scheduledConference.getStartDateTime();
        LocalDateTime scheduledEnd = scheduledConference.getEndDateTime();

        LocalDateTime actualStartDateTime = ConferenceInterval.getActualStartDateTime(scheduledStart);
        LocalDateTime actualEndDateTime = ConferenceInterval.getActualEndDateTime(scheduledEnd);
        ConferenceInterval scheduledInterval = new ConferenceInterval(actualStartDateTime, actualEndDateTime);
        ConferenceInterval plannedInterval = new ConferenceInterval(plannedStart, plannedEnd);

        return plannedInterval.contains(actualStartDateTime)
                || plannedInterval.contains(actualEndDateTime)
                || scheduledInterval.contains(plannedStart)
                || scheduledInterval.contains(plannedEnd);
    }

}
