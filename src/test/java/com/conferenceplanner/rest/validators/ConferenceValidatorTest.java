package com.conferenceplanner.rest.validators;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.RestTestHelper;
import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConferenceValidatorTest extends SpringContextTest {

    private static final int MIN_SYMBOLS_IN_CONFERENCE_NAME = 2;
    private static final int MAX_SYMBOLS_IN_CONFERENCE_NAME = 150;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ConferenceValidator validator;

    @Autowired
    private RestTestHelper testHelper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_start_date_time() {
        String startDateTimeString = null;
        String endDateTimeString = "12/12/2015 12:20";
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Start date time or end date time are are null");
        validator.validateDates(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validator_throws_ValidationException_if_empty_conference_start_date_time() {
        String endDateTimeString = null;
        String startDateTimeString = "12/12/2015 12:20";
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Start date time or end date time are are null");
        validator.validateDates(startDateTimeString, endDateTimeString);
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
        validator.validateDates(startDateTimeString, endDateTimeString);
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
        validator.validateDates(startDateTimeString, endDateTimeString);
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

        validator.validateDates(startDateTimeString, endDateTimeString);
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
        validator.validateDates(startDateTimeString, endDateTimeString);
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
        validator.validateDates(startDateTimeString, endDateTimeString);
    }

    @Test
    public void test_validate_conference_interval() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(3);
        String startDateTimeString = startDateTime.format(formatter);

        LocalDateTime endDateTime = startDateTime.plusDays(6);
        String endDateTimeString = endDateTime.format(formatter);

        ConferenceInterval interval = validator.validateDates(startDateTimeString, endDateTimeString);
        assertEquals(startDateTimeString, interval.getStartDateTime().format(formatter));
        assertEquals(endDateTimeString, interval.getEndDateTime().format(formatter));
    }

    @Test
    public void test_validateId_throws_ValidationException() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference id is null");
        validator.validateId(null);
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
    public void test_validator_throws_ValidationException_if_empty_conference_conference_room_id_list() {
        Conference conference = createConference("name");
        conference.setConferenceRoomIds(new ArrayList<>());
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room id list is null or empty");
        validator.validate(conference);
    }

    @Test
    public void test_validator_throws_ValidationException_if_null_conference_conference_room_id_list() {
        Conference conference = createConference("name");
        conference.setConferenceRoomIds(null);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference room id list is null or empty");
        validator.validate(conference);
    }

    @Test
    public void test_validator_throws_ValidationException_if_too_short_conference_name() {
        for (String nameString : getTooShortNames()) {
            try {
                validator.validate(createConference(nameString));
            } catch (ValidationException ex) {
                assertEquals("Invalid name length", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_throws_ValidationException_if_too_long_conference_name() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid name length");
        validator.validate(createConference(testHelper.getTooLongNameString(MAX_SYMBOLS_IN_CONFERENCE_NAME)));
    }

    @Test
    public void test_validate_conference() {
        try {
            for (String nameString : getValidNames()) {
                validator.validate(createConference(nameString));
            }
        } catch (Exception ex) {
            fail();
        }
    }

    private Conference createConference(String name) {
        Conference conference = new Conference();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.plusDays(3);
        String startDateTimeString = startDateTime.format(formatter);
        LocalDateTime endDateTime = startDateTime.plusDays(6);
        String endDateTimeString = endDateTime.format(formatter);

        conference.setStartDateTime(startDateTimeString);
        conference.setEndDateTime(endDateTimeString);
        conference.setName(name);

        List<Integer> conferenceRoomIds = new ArrayList<>();
        conferenceRoomIds.add(1);
        conference.setConferenceRoomIds(conferenceRoomIds);
        return conference;
    }

    private List<String> getTooShortNames() {
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("  ");
        strings.add("a");
        strings.add("0");
        strings.add(" a ");
        strings.add(" % ");
        return strings;
    }

    private List<String> getValidNames() {
        List<String> names = new ArrayList<>();
        names.add(testHelper.getValidNameString((MIN_SYMBOLS_IN_CONFERENCE_NAME)));
        names.add(testHelper.getValidNameString(MAX_SYMBOLS_IN_CONFERENCE_NAME));
        names.add(testHelper.getValidNameString(MAX_SYMBOLS_IN_CONFERENCE_NAME - 1));
        return names;
    }
}