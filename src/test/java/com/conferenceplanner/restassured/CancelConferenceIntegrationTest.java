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
import static org.hamcrest.Matchers.*;

public class CancelConferenceIntegrationTest {


    @Before
    public void setUp() throws Exception {
        delete("/conference-planner/test-util/clean-database");
        setupResources();
    }


    @Test
    public void test_cancelConference() throws Exception {
        Thread.sleep(10000);
        List<Integer> upcomingConferenceIds = ResourceManager.getUpcomingConferenceIds();
        String url = "/conference-planner/conferences/"
                .concat(upcomingConferenceIds.get(0).toString());
        given().
                contentType("application/json").
                when().
                put(url).
                then().
                assertThat().
                statusCode(201).
                assertThat().
                body(equalTo("Conference cancelled."));
    }

    private void setupResources() {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom);
        List<Integer> roomIds = ResourceManager.getAvailableConferenceRoomIds();
        Conference conference = ConferenceFixture.createConference();
        ResourceManager.createConference(conference, roomIds);
    }
}
