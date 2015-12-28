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

public class GetConferenceRoomAvailabilityIntegrationTest {

    @Before
    public void setUp() {
        delete("/conference-planner/test-util/clean-database");
        setupResources();
    }

    @Test
    public void test_getConferenceRoomAvailability() {
        List<Integer> allConferenceRoomIds = ResourceManager.getAllConferenceRoomIds();
        assertEquals(2, allConferenceRoomIds.size());

        String url1 = buildGetConferenceRoomAvailabilityUrl(allConferenceRoomIds.get(0));
        String url2 = buildGetConferenceRoomAvailabilityUrl(allConferenceRoomIds.get(1));

        when().
                get(url1).
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                assertThat().
                body("conferenceRoom.name", is("M/S Baltic Queen conference")).
                assertThat().
                body("conferenceRoomAvailabilityItems[0].conference.name", is("Devoxx 2016")).
                assertThat().
                body("conferenceRoomAvailabilityItems[0].availableSeats", is(199)).
                assertThat().
                body("conferenceRoomAvailabilityItems[1].conference.name", is("JavaOne")).
                assertThat().
                body("conferenceRoomAvailabilityItems[1].availableSeats", is(199));

        when().
                get(url2).
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                assertThat().
                body("conferenceRoom.name", is("Radisson Blu conference")).
                assertThat().
                body("conferenceRoomAvailabilityItems[0].conference.name", is("Devoxx 2016")).
                assertThat().
                body("conferenceRoomAvailabilityItems[0].availableSeats", is(200)).
                assertThat().
                body("conferenceRoomAvailabilityItems[1].conference.name", is("JavaOne")).
                assertThat().
                body("conferenceRoomAvailabilityItems[1].availableSeats", is(200));
    }

    private void setupResources() {
        ConferenceRoom conferenceRoom1 = ConferenceRoomFixture.createConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom1);
        ConferenceRoom conferenceRoom2 = ConferenceRoomFixture.createAnotherConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom2);
        List<Integer> roomIds = ResourceManager.getAvailableConferenceRoomIds();
        Conference conference1 = ConferenceFixture.createConference();
        Conference conference2 = ConferenceFixture.createAnotherConference();
        ResourceManager.createConference(conference1, roomIds);
        ResourceManager.createConference(conference2, roomIds);
        List<Integer> availableConferenceIds = ResourceManager.getAvailableConferenceIds();
        assertEquals(2, availableConferenceIds.size());
        Participant participant1 = ParticipantFixture.createParticipant(availableConferenceIds.get(0));
        Participant participant2 = ParticipantFixture.createAnotherParticipant(availableConferenceIds.get(1));
        ResourceManager.addParticipant(participant1);
        ResourceManager.addParticipant(participant2);
    }

    private String buildGetConferenceRoomAvailabilityUrl(Integer conferenceRoomId) {
        return  "/conference-planner/conference-rooms/"
                .concat(conferenceRoomId.toString())
                .concat("/conference-room-availability");
    }

}
