package com.conferenceplanner.core.services.integration.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.services.ConferenceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Component
public class ConferenceServiceIntegrationTestHelper {

    @Autowired
    private ConferenceChecker conferenceChecker;

    private LocalDateTime now = LocalDateTime.now();


    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public void assertGetUpcomingConferencesResult(List<Conference> conferences) {
        for (Conference conference: conferences) {
            assertFalse(conference.isCancelled());
            assertTrue(conference.getStartDateTime().isAfter(now));
        }
    }

    public void assertGetAvailableConferencesResult(List<Conference> conferences) {
        for (Conference conference: conferences) {
            assertTrue(conferenceChecker.isAvailable(conference));
        }
    }

    public void assertRegisterConferenceResult(Conference conference, List<ConferenceRoom> rooms) {
        List<ConferenceRoomAvailabilityItem> availabilityItems = conference.getConferenceRoomAvailabilityItems();
        assertEquals(rooms.size(), availabilityItems.size());

        for (ConferenceRoomAvailabilityItem item: availabilityItems) {
            assertNotNull(item.getId());
            assertEquals(conference, item.getConference());
            ConferenceRoom room = item.getConferenceRoom();
            assertTrue(rooms.contains(room));
            assertEquals(room.getMaxSeats(), item.getAvailableSeats());
            rooms.remove(room);
        }
        for (ConferenceRoom room: rooms) {
            assertTrue(room.getConferences().contains(conference));
            assertEquals(1, room.getConferences().size());
            assertEquals(1, room.getConferenceRoomAvailabilityItems().size());
        }
    }

    public List<Integer> getConferenceRoomIds(List<ConferenceRoom> conferenceRooms) {
        return conferenceRooms.stream().map(ConferenceRoom::getId).collect(Collectors.toList());
    }


}
