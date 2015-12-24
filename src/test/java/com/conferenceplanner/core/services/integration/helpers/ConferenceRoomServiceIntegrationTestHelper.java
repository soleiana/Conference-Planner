package com.conferenceplanner.core.services.integration.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.services.CommonTestHelper;
import com.conferenceplanner.core.services.ConferenceRoomChecker;
import org.junit.Assert;
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

    @Autowired
    private CommonTestHelper commonTestHelper;

    private LocalDateTime now;

    public void setNow(LocalDateTime now) {
        this.now = now;
    }


    public void assertGetAvailableConferenceRoomsResult(List<ConferenceRoom> conferenceRooms, Conference plannedConference) {
       conferenceRooms.stream()
               .map(room -> conferenceRoomChecker.isAvailable(room, plannedConference))
               .forEach(Assert::assertTrue);
    }

    public void assertGetConferenceRoomAvailabilityItemsResult(List<ConferenceRoomAvailabilityItem> availabilityItems) {
        availabilityItems.stream()
                .map(ConferenceRoomAvailabilityItem::getConference)
                .map(Conference::isCancelled)
                .forEach(Assert::assertFalse);

        availabilityItems.stream()
                .map(ConferenceRoomAvailabilityItem::getConference)
                .map(Conference::getStartDateTime)
                .map(date -> date.isAfter(now))
                .forEach(Assert::assertTrue);

    }

    public List<Integer> getConferenceRoomIds(List<ConferenceRoom> conferenceRooms) {
        return commonTestHelper.getConferenceRoomIds(conferenceRooms);
    }

}
