package com.conferenceplanner.restassured;

import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.fixtures.ConferenceFixture;
import com.conferenceplanner.rest.fixtures.ConferenceRoomFixture;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class CreateConferenceIntegrationTest {

    private static final String CONFERENCE_START_DATE_TIME = LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    private static final String CONFERENCE_END_DATE_TIME = LocalDateTime.now().plusDays(6).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));


    @Before
    public void setUp() {
        delete("/conference-planner/test-util/clean-database");
        setupResources();
    }

    @Test
    public void test_createConference() throws Exception {

        String urlString = buildUrlString();

        URL url = new URL(urlString);
        List<Map<String, Object>> availableConferenceRooms = getAvailableConferenceRooms(url);
        List<Integer> roomIds = availableConferenceRooms.stream().map(e -> e.get("id")).map(e -> (Integer)e).collect(Collectors.toList());

        Conference conference = ConferenceFixture.createConference(roomIds);
        given().
                contentType("application/json").
                body(conference).
                when().
                post("/conference-planner/conferences").
                then().
                assertThat().
                statusCode(201).
                assertThat().
                body(equalTo("Conference created."));
    }

    private void setupResources() {
        ConferenceRoom conferenceRoom1 = ConferenceRoomFixture.createConferenceRoom();
        ConferenceRoom conferenceRoom2 = ConferenceRoomFixture.createAnotherConferenceRoom();
        createConferenceRoom(conferenceRoom1);
        createConferenceRoom(conferenceRoom2);
    }

    private void createConferenceRoom(ConferenceRoom conferenceRoom) {
        given().
                contentType("application/json").
                body(conferenceRoom).
                when().
                post("/conference-planner/conference-rooms").
                then().
                assertThat().
                statusCode(201);
    }

    private String buildUrlString() {
        return "http://localhost:8080/conference-planner/conference-rooms?"
                .concat("conferenceStartDateTime=")
                .concat(CONFERENCE_START_DATE_TIME)
                .concat("&")
                .concat("conferenceEndDateTime=")
                .concat(CONFERENCE_END_DATE_TIME);
    }

    private  List<Map<String, Object>> getAvailableConferenceRooms(URL url) {
        return when().
                get(url).
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                extract().
                path("availableConferenceRooms");
    }

}
