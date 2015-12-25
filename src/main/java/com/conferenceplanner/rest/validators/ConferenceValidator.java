package com.conferenceplanner.rest.validators;


import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import com.conferenceplanner.rest.parsers.ConferenceParser;
import com.conferenceplanner.rest.parsers.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ConferenceValidator {

    private static final int MIN_TIME_BEFORE_CONFERENCE_START_IN_DAYS = 2;
    private static final int MIN_CONFERENCE_DURATION_IN_HOURS = 2;
    private static final int MAX_CONFERENCE_DURATION_IN_DAYS = 7;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(ConferenceParser.DATE_TIME_FORMAT_PATTERN);

    @Autowired
    private ConferenceRoomValidator conferenceRoomValidator;

    public void validate(Conference conference) {
        String nameString = conference.getName();
        String startDateTimeString = conference.getStartDateTime();
        String endDateTimeString = conference.getEndDateTime();
        List<Integer> conferenceRoomIds = conference.getConferenceRoomIds();

        conferenceRoomValidator.validateIds(conferenceRoomIds);
        validate(startDateTimeString, endDateTimeString);
        validate(nameString);
    }

    public ConferenceInterval validate(String startDateTimeString, String endDateTimeString) {
        ConferenceInterval interval;

        if (startDateTimeString == null || endDateTimeString == null) {
            throw new ValidationException("Start date time or end date time are are null");
        }

        try {
            interval = ConferenceParser.parse(startDateTimeString, endDateTimeString);
            validate(interval);

        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
        return interval;
    }

    public void validateId(Integer conferenceId) {
        if (conferenceId == null) {
            throw new ValidationException("Conference id is null");
        }
    }

    private void validate(ConferenceInterval interval) {
        LocalDateTime startDateTime = interval.getStartDateTime();
        LocalDateTime endDateTime = interval.getEndDateTime();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime minStartDateTime = now.plusDays(MIN_TIME_BEFORE_CONFERENCE_START_IN_DAYS);

        if (startDateTime.isBefore(minStartDateTime)) {
            throw new ValidationException(String.format("Conference must start after %s", minStartDateTime.format(DATE_TIME_FORMATTER)));
        }

        if (endDateTime.isBefore(startDateTime)) {
            throw new ValidationException(String.format("Conference end dateTime %s is before start dateTime %s",
                    endDateTime.format(DATE_TIME_FORMATTER), startDateTime.format(DATE_TIME_FORMATTER)));
        }

        long hours = startDateTime.until(endDateTime, ChronoUnit.HOURS);

        if (hours < MIN_CONFERENCE_DURATION_IN_HOURS) {
            throw new ValidationException(String.format("Conference duration is %s hours, less than min of %s hour(s)", hours, MIN_CONFERENCE_DURATION_IN_HOURS));
        }

        long days = startDateTime.until(endDateTime, ChronoUnit.DAYS);

        if (days > MAX_CONFERENCE_DURATION_IN_DAYS) {
            throw new ValidationException(String.format("Conference duration is %s days, more than max of %s day(s)", days, MAX_CONFERENCE_DURATION_IN_DAYS));
        }
    }

    private void validate(String nameString) {
        if (nameString == null) {
            throw new ValidationException("Name is null");
        }
        try {
            ConferenceParser.parse(nameString);
        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

}
