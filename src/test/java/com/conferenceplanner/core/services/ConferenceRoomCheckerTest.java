package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.fixtures.ConferenceFixture;
import com.conferenceplanner.core.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class ConferenceRoomCheckerTest {

    private ConferenceRoomChecker checker;
    private ConferenceRoom conferenceRoom;
    private Conference plannedConference;

    @Before
    public void setUp() throws Exception {
        checker = new ConferenceRoomChecker();
        conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        plannedConference = ConferenceFixture.createConference();
    }

    @Test
    public void test_isAvailable_is_true_if_no_conferences_in_conference_room() {
        boolean result = checker.isAvailable(conferenceRoom, plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_isAvailable_is_true_if_only_cancelled_conferences_in_conference_room(){
        List<Conference> conferences = ConferenceFixture.createCancelledConferences();
        conferenceRoom.setConferences(conferences);
        boolean result = checker.isAvailable(conferenceRoom, plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_isAvailable_is_true_if_no_overlapping_conferences_in_conference_room() {
        List<Conference> conferences = ConferenceFixture.createNonOverlappingConferences();
        conferenceRoom.setConferences(conferences);
        boolean result = checker.isAvailable(conferenceRoom, plannedConference);
        assertTrue(result);
    }

    @Test
    public void test_isAvailable_is_false_if_an_overlapping_conference_in_conference_room() {
        List<Conference> conferences = ConferenceFixture.createMixedConferences();
        conferenceRoom.setConferences(conferences);
        boolean result = checker.isAvailable(conferenceRoom, plannedConference);
        assertFalse(result);
    }

    @Test
    public void test_compare_is_true() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(10);
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(20);
        assertTrue(checker.compare(room1, room2));
    }

    @Test
    public void test_compare_is_false() {
        ConferenceRoom room1 = ConferenceRoomFixture.createConferenceRoom(10);
        ConferenceRoom room2 = ConferenceRoomFixture.createConferenceRoom(10);
        room2.setName(room2.getName()+"a");
        assertFalse(checker.compare(room1, room2));
    }

}