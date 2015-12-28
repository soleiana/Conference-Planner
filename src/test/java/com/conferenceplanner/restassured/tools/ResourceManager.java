package com.conferenceplanner.restassured.tools;

import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import com.conferenceplanner.rest.domain.Participant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class ResourceManager {

    private static final String CONFERENCE_START_DATE_TIME = LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    private static final String CONFERENCE_END_DATE_TIME = LocalDateTime.now().plusDays(6).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

    public static void createConferenceRoom(ConferenceRoom conferenceRoom) {
        given().
                contentType("application/json").
                body(conferenceRoom).
                when().
                post("/conference-planner/conference-rooms").
                then().
                assertThat().
                statusCode(201);
    }

    public static List<Integer> getAllConferenceRoomIds() {
        List<Map<String, Object>> allConferenceRooms = when().
                get("/conference-planner/conference-rooms").
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                extract().
                path("conferenceRooms");
        return extractIds(allConferenceRooms, "id");
    }

    public static List<Integer> getAvailableConferenceRoomIds() {
        String url = buildGetAvailableConferenceRoomsUrlString();
        List<Map<String, Object>> availableConferenceRooms =  when().
                get(url).
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                extract().
                path("conferenceRooms");
        return extractIds(availableConferenceRooms, "id");
    }

    public static void createConference(Conference conference) {
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

    public static void createConference(Conference conference, List<Integer> conferenceRoomIds) {
        conference.setConferenceRoomIds(conferenceRoomIds);
        createConference(conference);
    }

    public static void addParticipant(Participant participant) {
        given().
                contentType("application/json").
                body(participant).
                when().
                post("/conference-planner/participants").
                then().
                assertThat().
                statusCode(201).
                assertThat().
                body(equalTo("Participant added."));
    }

    public static List<Integer> getUpcomingConferenceIds() {
        List<Map<String, Object>> upcomingConferences = when().
        get("/conference-planner/conferences/upcoming").
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                extract().
                path("conferences");
        return extractIds(upcomingConferences, "id");
    }

    public static List<Integer> getAvailableConferenceIds() {
        List<Map<String, Object>> availableConferences = when().
                get("/conference-planner/conferences/available").
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                extract().
                path("conferences");
        return extractIds(availableConferences, "id");
    }

    public static List<Integer> getParticipantIds(Integer conferenceId) {
        String url = buildGetParticipantsUrlString(conferenceId);
        List<Map<String, Object>> participants = when().
                get(url).
                then().
                contentType("application/json").
                assertThat().
                statusCode(200).
                assertThat().
                body(notNullValue()).
                extract().
                path("participants");
        return extractIds(participants, "id");
    }

    private static String buildGetAvailableConferenceRoomsUrlString() {
        return  "/conference-planner/conference-rooms/available?"
                .concat("conferenceStartDateTime=")
                .concat(CONFERENCE_START_DATE_TIME)
                .concat("&")
                .concat("conferenceEndDateTime=")
                .concat(CONFERENCE_END_DATE_TIME);
    }

    private static String buildGetParticipantsUrlString(Integer conferenceId) {
        return "/conference-planner/conferences/"
                .concat(conferenceId.toString())
                .concat("/participants");
    }

    private static List<Integer> extractIds(List<Map<String, Object>> outputMap, String idName){
        return outputMap.stream()
                .map(e -> e.get(idName))
                .map(e -> (Integer)e)
                .collect(Collectors.toList());
    }

}
