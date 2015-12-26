package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.fixtures.ConferenceFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomAvailabilityItemFixture;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConferenceCheckerTest {

    private ConferenceChecker checker;
    private Conference conference;

    @Before
    public void setup() {
        checker = new ConferenceChecker();
        conference = ConferenceFixture.createConference();
    }

    @Test
    public void test_isAvailable_is_false_if_no_conference_room_has_available_seats() {
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createFullyOccupiedConferenceRooms(3);
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        boolean result = checker.isAvailable(conference);
        assertFalse(result);
    }

    @Test
    public void test_isAvailable_is_true_if_one_conference_room_has_available_seats() {
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createPartiallyOccupiedConferenceRooms(3);
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        boolean result = checker.isAvailable(conference);
        assertTrue(result);
    }

    @Test
    public void test_isAvailable_is_true_if_all_conference_rooms_have_available_seats() {
        List<ConferenceRoomAvailabilityItem> availabilityItems =
                ConferenceRoomAvailabilityItemFixture.createConferenceRoomsWithAvailableSeats(3);
        conference.setConferenceRoomAvailabilityItems(availabilityItems);
        boolean result = checker.isAvailable(conference);
        assertTrue(result);
    }

}