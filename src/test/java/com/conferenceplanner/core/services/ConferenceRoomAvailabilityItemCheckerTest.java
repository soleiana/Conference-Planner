package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.core.services.fixtures.ConferenceFixture;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConferenceRoomAvailabilityItemCheckerTest {

    private ConferenceRoomAvailabilityItemChecker checker;
    private ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem;

    @Before
    public void setUp() throws Exception {
        checker = new ConferenceRoomAvailabilityItemChecker();
        conferenceRoomAvailabilityItem = new ConferenceRoomAvailabilityItem(5);
    }

    @Test
    public void test_isActual_is_false_if_conference_startDateTime_is_in_past() {
        Conference conference = ConferenceFixture.createOngoingConference();
        conferenceRoomAvailabilityItem.setConference(conference);
        boolean result = checker.isActual(conferenceRoomAvailabilityItem);
        assertFalse(result);
    }

    @Test
    public void test_isActual_is_false_if_conference_is_cancelled() {
        Conference conference = ConferenceFixture.createCancelledConference();
        conferenceRoomAvailabilityItem.setConference(conference);
        boolean result = checker.isActual(conferenceRoomAvailabilityItem);
        assertFalse(result);
    }

    @Test
    public void test_isActual_is_true_if_conference_is_upcoming() {
        Conference conference = ConferenceFixture.createUpcomingConference();
        conferenceRoomAvailabilityItem.setConference(conference);
        boolean result = checker.isActual(conferenceRoomAvailabilityItem);
        assertTrue(result);
    }
}