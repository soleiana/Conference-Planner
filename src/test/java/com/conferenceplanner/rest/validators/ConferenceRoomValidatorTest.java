package com.conferenceplanner.rest.validators;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.domain.ConferenceRoom;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ConferenceRoomValidatorTest extends SpringContextTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ConferenceRoomValidator validator;

    @Test
    public void test_validator_throws_ValidationException_if_null_parameter() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setMaxSeats(20);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("One ore more parameters are null or empty");
        assertNull(conferenceRoom.getLocation());
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_empty_parameter() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setLocation("");
        conferenceRoom.setMaxSeats(20);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("One ore more parameters are null or empty");
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
    public void test_validate() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A aaa aaa  conference");
        conferenceRoom.setLocation("A/A  aaa  aaa");
        conferenceRoom.setMaxSeats(5);

        boolean result = validator.validate(conferenceRoom);
        assertTrue(result);
    }
}