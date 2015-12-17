package com.conferenceplanner.rest.validators;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class ConferenceValidatorTest extends SpringContextTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ConferenceValidator validator;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_start_date_time() {
        String startDateTimeString = null;
        String endDateTimeString = "12/12/2015 12:20";
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("One ore more parameters are null or empty");
        validator.validate(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validator_throws_ValidationException_if_empty_conference_start_date_time() {
        String startDateTimeString = "";
        String endDateTimeString = "12/12/2015 12:20";
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("One ore more parameters are null or empty");
        validator.validate(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_id() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference id is null");
        validator.validateId(null);
    }

    @Test
    public void test_validator_throws_ValidationException_if_parser_fails() {
        String startDateTimeString = "12/12/2015 12:00";
        String endDateTimeString = "12/12/2015 15:60";
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid start dateTime or end dateTime format");
        validator.validate(startDateTimeString, endDateTimeString);
    }


    @Test
    public void test_validator_throws_ValidationException_if_too_few_days_before_conference_start() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(1);
        String startDateTimeString = startDateTime.format(formatter);

        LocalDateTime endDateTime = startDateTime.plusHours(3);
        String endDateTimeString = endDateTime.format(formatter);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference must start after " + now.plusDays(2).format(formatter));
        validator.validate(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validator_throws_ValidationException_if_conference_end_is_before_start() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(3);
        String startDateTimeString = startDateTime.format(formatter);

        LocalDateTime endDateTime = startDateTime.minusMinutes(1);
        String endDateTimeString = endDateTime.format(formatter);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(String.format("Conference end dateTime %s is before start dateTime %s",
                endDateTime.format(formatter), startDateTime.format(formatter)));

        validator.validate(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_short_conference() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(3);
        String startDateTimeString = startDateTime.format(formatter);

        LocalDateTime endDateTime = startDateTime.plusHours(1);
        String endDateTimeString = endDateTime.format(formatter);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(String.format("Conference duration is %s hours, less than min of %s hour(s)", 1, 2));
        validator.validate(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_long_conference() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(3);
        String startDateTimeString = startDateTime.format(formatter);

        LocalDateTime endDateTime = startDateTime.plusDays(8);
        String endDateTimeString = endDateTime.format(formatter);

        expectedException.expect(ValidationException.class);
        expectedException.expectMessage(String.format(String.format("Conference duration is %s days, more than max of %s day(s)", 8, 7)));
        validator.validate(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validate_conference_parameters() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(3);
        String startDateTimeString = startDateTime.format(formatter);

        LocalDateTime endDateTime = startDateTime.plusDays(6);
        String endDateTimeString = endDateTime.format(formatter);

        ConferenceInterval interval = validator.validate(startDateTimeString, endDateTimeString);
        assertEquals(startDateTimeString, interval.getStartDateTime().format(formatter));
        assertEquals(endDateTimeString, interval.getEndDateTime().format(formatter));
    }

    @Test
    public void test_validate_conference_id() {
        boolean result = validator.validateId(1);
        assertTrue(result);
    }

}