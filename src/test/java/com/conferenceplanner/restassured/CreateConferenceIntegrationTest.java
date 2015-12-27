package com.conferenceplanner.restassured;

import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.fixtures.ConferenceFixture;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.restassured.tools.ResourceManager;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.*;


public class CreateConferenceIntegrationTest {


    @Before
    public void setUp() {
        delete("/conference-planner/test-util/clean-database");
        setupResources();
    }

    @Test
    public void test_createConference() {
        Conference conference = ConferenceFixture.createConference();
        List<Integer> roomIds = ResourceManager.getAvailableConferenceRoomIds();
        conference.setConferenceRoomIds(roomIds);
        ResourceManager.createConference(conference);
    }

    private void setupResources() {
        ConferenceRoom conferenceRoom1 = ConferenceRoomFixture.createConferenceRoom();
        ConferenceRoom conferenceRoom2 = ConferenceRoomFixture.createAnotherConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom1);
        ResourceManager.createConferenceRoom(conferenceRoom2);
    }
}
