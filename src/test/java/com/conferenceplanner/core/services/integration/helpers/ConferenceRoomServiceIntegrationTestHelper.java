package com.conferenceplanner.core.services.integration.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.services.ConferenceRoomChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Component
public class ConferenceRoomServiceIntegrationTestHelper {

    @Autowired
    private ConferenceRoomChecker conferenceRoomChecker;

    private LocalDateTime now;


    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public void assertGetAvailableConferenceRoomsResult(List<ConferenceRoom> conferenceRooms, Conference plannedConference) {
        for (ConferenceRoom room: conferenceRooms) {
            assertTrue(conferenceRoomChecker.isAvailable(room, plannedConference));
        }
    }

    public void assertGetConferenceRoomAvailabilityItemsResult(List<ConferenceRoomAvailabilityItem> availabilityItems) {
        for (ConferenceRoomAvailabilityItem item: availabilityItems) {
            assertFalse(item.getConference().isCancelled());
            assertTrue(item.getConference().getStartDateTime().isAfter(now));
        }
    }

    public List<Integer> getConferenceRoomIds(List<ConferenceRoom> conferenceRooms) {
        return conferenceRooms.stream().map(ConferenceRoom::getId).collect(Collectors.toList());
    }

}
