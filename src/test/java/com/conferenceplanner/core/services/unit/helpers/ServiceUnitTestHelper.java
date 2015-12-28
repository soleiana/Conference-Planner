package com.conferenceplanner.core.services.unit.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.services.CommonTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Component
public class ServiceUnitTestHelper {

    @Autowired
    private CommonTestHelper commonTestHelper;

    public void assertRegisterConferenceResult(Conference conference, List<ConferenceRoom> rooms) {
        List<ConferenceRoomAvailabilityItem> availabilityItems = conference.getConferenceRoomAvailabilityItems();
        assertEquals(rooms.size(), availabilityItems.size());

        availabilityItems.stream()
                .forEach(item -> assertConferenceRoomAvailabilityItem(item, conference, rooms));
        rooms.stream()
                .forEach(this::assertConferenceRoom);
    }

    public List<Integer> getConferenceRoomIds(List<ConferenceRoom> conferenceRooms) {
        return commonTestHelper.getConferenceRoomIds(conferenceRooms);
    }

    private void assertConferenceRoom(ConferenceRoom room) {
        assertEquals(1, room.getConferenceRoomAvailabilityItems().size());
    }

    private void assertConferenceRoomAvailabilityItem(ConferenceRoomAvailabilityItem item, Conference conference, List<ConferenceRoom> rooms) {
        assertEquals(conference, item.getConference());
        ConferenceRoom room = item.getConferenceRoom();
        assertTrue(rooms.contains(room));
        assertEquals(room.getMaxSeats(), item.getAvailableSeats());
        rooms.remove(room);
    }
}
