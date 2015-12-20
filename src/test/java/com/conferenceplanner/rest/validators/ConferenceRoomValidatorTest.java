package com.conferenceplanner.rest.validators;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConferenceRoomValidatorTest extends SpringContextTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ConferenceRoomValidator validator;

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_room_location() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setMaxSeats(20);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("One ore more parameters are null");
        assertNull(conferenceRoom.getLocation());
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_room_id() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room id is null");
        validator.validateId(null);
    }

    @Test
    public void test_validator_throws_ValidationException_if_null_max_seats() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setLocation("location");

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("One ore more parameters are null");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_few_seats() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setLocation("location");
        conferenceRoom.setMaxSeats(4);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid max seats");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_many_seats() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setLocation("location");
        conferenceRoom.setMaxSeats(2001);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid max seats");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_parser_fails() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A aaa aaa  conference");
        conferenceRoom.setLocation("A/A  aaa  aab");
        conferenceRoom.setMaxSeats(5);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid name or location");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validate_conference_room_parameters() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A aaa aaa  conference");
        conferenceRoom.setLocation("A/A  aaa  aaa");
        conferenceRoom.setMaxSeats(5);
        try {
            validator.validate(conferenceRoom);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void test_validateId() {
        try {
            validator.validateId(1);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void test_validateId_throws_ValidationException() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room id is null");
        validator.validateId(null);

    }
}

