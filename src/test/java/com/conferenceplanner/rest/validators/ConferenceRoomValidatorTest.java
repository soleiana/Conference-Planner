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
    public void test_validator_throws_ValidationException_if_null_conference_room_location() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setMaxSeats(20);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room name or location is null");
        assertNull(conferenceRoom.getLocation());
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_room_name() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setLocation("location");
        conferenceRoom.setMaxSeats(20);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room name or location is null");
        assertNull(conferenceRoom.getName());
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_null_max_seats() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("name");
        conferenceRoom.setLocation("location");
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room max seats number is null");
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
    public void test_validator_throws_ValidationException_if_conference_room_name_and_location_do_mot_match() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A aaa conference");
        conferenceRoom.setLocation("A/A aab");
        conferenceRoom.setMaxSeats(5);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid name or location");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_short_conference_room_location() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A conference");
        conferenceRoom.setLocation("A/A");
        conferenceRoom.setMaxSeats(5);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid location length");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_long_conference_room_location() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A aa aa aa aa aa conference");
        conferenceRoom.setLocation("A/A aa aa aa aa aa");
        conferenceRoom.setMaxSeats(5);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid location length");
        validator.validate(conferenceRoom);
    }

    @Test
    public void test_validate_conferenceRoom() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("A/A aaa conference");
        conferenceRoom.setLocation("A/A  aaa");
        conferenceRoom.setMaxSeats(5);
        try {
            validator.validate(conferenceRoom);
        } catch (Exception ex) {
            fail();
        }
    }

}

