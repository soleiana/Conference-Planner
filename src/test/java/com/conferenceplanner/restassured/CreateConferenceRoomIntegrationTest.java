package com.conferenceplanner.restassured;

import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.restassured.tools.ResourceManager;
import org.junit.Before;
import org.junit.Test;


import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateConferenceRoomIntegrationTest {

    @Before
    public void setUp() {
        delete("/conference-planner/test-util/clean-database");
    }

    @Test
    public void test_createConferenceRoom() {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        given().
                contentType("application/json").
                body(conferenceRoom).
                when().
                post("/conference-planner/conference-rooms").
                then().
                contentType("text/plain").
                assertThat().
                statusCode(201).
                assertThat().
                body(equalTo("Conference room created."));
    }
}
