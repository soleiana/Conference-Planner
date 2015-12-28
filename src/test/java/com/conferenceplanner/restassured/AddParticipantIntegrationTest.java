package com.conferenceplanner.restassured;

import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.fixtures.ConferenceFixture;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import com.conferenceplanner.rest.fixtures.ParticipantFixture;
import com.conferenceplanner.restassured.tools.ResourceManager;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AddParticipantIntegrationTest {

    @Before
    public void setUp() throws Exception {
        delete("/conference-planner/test-util/clean-database");
        setupResources();
    }

    @Test
    public void test_addParticipant() {
        List<Integer> availableConferenceIds = ResourceManager.getAvailableConferenceIds();
        assertEquals(2, availableConferenceIds.size());
        Participant participant = ParticipantFixture.createParticipant(availableConferenceIds.get(1));

        given().
                contentType("application/json").
                body(participant).
                when().
                post("/conference-planner/participants").
                then().
                contentType("text/plain").
                assertThat().
                statusCode(201).
                assertThat().
                body(equalTo("Participant added."));
    }

    private void setupResources() {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom);
        List<Integer> roomIds = ResourceManager.getAvailableConferenceRoomIds();
        Conference conference1 = ConferenceFixture.createConference();
        Conference conference2 = ConferenceFixture.createAnotherConference();
        ResourceManager.createConference(conference1, roomIds);
        ResourceManager.createConference(conference2, roomIds);
    }
}
