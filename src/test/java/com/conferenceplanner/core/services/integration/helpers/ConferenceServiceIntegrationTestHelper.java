package com.conferenceplanner.core.services.integration.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.services.CommonTestHelper;
import com.conferenceplanner.core.services.ConferenceChecker;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.Assert.*;

@Component
public class ConferenceServiceIntegrationTestHelper {

    @Autowired
    private ConferenceChecker conferenceChecker;

    @Autowired
    private CommonTestHelper commonTestHelper;

    private LocalDateTime now = LocalDateTime.now();


    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public void assertGetUpcomingConferencesResult(List<Conference> conferences) {
        conferences.stream()
                .map(Conference::isCancelled)
                .forEach(Assert::assertFalse);

        conferences.stream()
                .map(Conference::getStartDateTime)
                .map(date -> date.isAfter(now))
                .forEach(Assert::assertTrue);
    }

    public void assertGetAvailableConferencesResult(List<Conference> conferences) {
       conferences.stream()
               .map(conferenceChecker::isAvailable)
               .forEach(Assert::assertTrue);
    }

    public void assertRegisterConferenceResult(Conference conference, List<ConferenceRoom> rooms) {
        List<ConferenceRoomAvailabilityItem> availabilityItems = conference.getConferenceRoomAvailabilityItems();
        assertEquals(rooms.size(), availabilityItems.size());

        availabilityItems.stream()
                .forEach(item -> assertConferenceRoomAvailabilityItem(item, conference, rooms));
        rooms.stream()
                .forEach(room -> assertConferenceRoom(room, conference));
    }

    public List<Integer> getConferenceRoomIds(List<ConferenceRoom> conferenceRooms) {
        return commonTestHelper.getConferenceRoomIds(conferenceRooms);
    }

    private void assertConferenceRoom(ConferenceRoom room, Conference conference) {
        assertTrue(room.getConferences().contains(conference));
        assertEquals(1, room.getConferences().size());
        assertEquals(1, room.getConferenceRoomAvailabilityItems().size());
    }

    private void assertConferenceRoomAvailabilityItem(ConferenceRoomAvailabilityItem item, Conference conference, List<ConferenceRoom> rooms) {
        assertNotNull(item.getId());
        assertEquals(conference, item.getConference());
        ConferenceRoom room = item.getConferenceRoom();
        assertTrue(rooms.contains(room));
        assertEquals(room.getMaxSeats(), item.getAvailableSeats());
        rooms.remove(room);
    }
}
