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

public class RemoveParticipantIntegrationTest {

    @Before
    public void setUp() throws Exception {
        delete("/conference-planner/test-util/clean-database");
        setupResources();
    }
    
    @Test
    public void test_removeParticipant() {
        List<Integer> upcomingConferenceIds = ResourceManager.getUpcomingConferenceIds();
        assertEquals(2, upcomingConferenceIds.size());
        int conferenceId = upcomingConferenceIds.get(1);
        List<Integer> participantIds = ResourceManager.getParticipantIds(conferenceId);
        assertEquals(2, participantIds.size());
        Participant participant = ParticipantFixture.createParticipant(conferenceId, participantIds.get(1));

        given().
                contentType("application/json").
                body(participant).
                when().
                delete("/conference-planner/participants").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                body(equalTo("Participant removed."));
    }

    private void setupResources() {
        ConferenceRoom conferenceRoom = ConferenceRoomFixture.createConferenceRoom();
        ResourceManager.createConferenceRoom(conferenceRoom);
        List<Integer> roomIds = ResourceManager.getAvailableConferenceRoomIds();
        Conference conference1 = ConferenceFixture.createConference();
        Conference conference2 = ConferenceFixture.createAnotherConference();
        ResourceManager.createConference(conference1, roomIds);
        ResourceManager.createConference(conference2, roomIds);
        List<Integer> availableConferenceIds = ResourceManager.getAvailableConferenceIds();
        assertEquals(2, availableConferenceIds.size());
        Participant participant1 = ParticipantFixture.createParticipant(availableConferenceIds.get(1));
        Participant participant2 = ParticipantFixture.createAnotherParticipant(availableConferenceIds.get(1));
        ResourceManager.addParticipant(participant1);
        ResourceManager.addParticipant(participant2);
    }
}
