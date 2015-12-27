package com.conferenceplanner.restassured;

import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.restassured.tools.ResourceManager;
import org.junit.Before;
import org.junit.Test;


import static com.jayway.restassured.RestAssured.*;

public class CreateConferenceRoomIntegrationTest {

    @Before
    public void setUp() {
        delete("/conference-planner/test-util/clean-database");
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom);
    }
}
