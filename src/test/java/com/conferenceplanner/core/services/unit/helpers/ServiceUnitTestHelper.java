package com.conferenceplanner.core.services.unit.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Component
public class ServiceUnitTestHelper {

    public void assertRegisterConferenceResult(Conference conference, List<ConferenceRoom> rooms) {
        List<ConferenceRoomAvailabilityItem> availabilityItems = conference.getConferenceRoomAvailabilityItems();
        assertEquals(rooms.size(), availabilityItems.size());

        for (ConferenceRoomAvailabilityItem item: availabilityItems) {
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
