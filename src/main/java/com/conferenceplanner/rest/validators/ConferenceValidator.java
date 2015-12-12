package com.conferenceplanner.rest.validators;


import com.conferenceplanner.rest.domain.ConferenceInterval;
import com.conferenceplanner.rest.parsers.ConferenceParser;
import com.conferenceplanner.rest.parsers.ParserException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ConferenceValidator {

    private static final int MIN_TIME_BEFORE_CONFERENCE_START_IN_DAYS = 1;
    private static final int MIN_CONFERENCE_DURATION_IN_HOURS = 2;
    private static final int MAX_CONFERENCE_DURATION_IN_DAYS = 7;

    public ConferenceInterval validate(String startDateTimeString, String endDateTimeString) {
        ConferenceInterval interval;

        if (startDateTimeString == null || startDateTimeString.isEmpty()
                || endDateTimeString == null || endDateTimeString.isEmpty()) {
            throw new ValidationException("One ore more parameters are null or empty");
        }

        try {
            interval = ConferenceParser.parse(startDateTimeString, endDateTimeString);
            validate(interval);

        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
        return interval;
    }

    private void validate(ConferenceInterval interval) {
        LocalDateTime startDateTime = interval.getStartDateTime();
        LocalDateTime endDateTime = interval.getEndDateTime();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime minStartDateTime = now.plusDays(MIN_TIME_BEFORE_CONFERENCE_START_IN_DAYS);

        if (startDateTime.isBefore(minStartDateTime)) {
            throw new ValidationException(String.format("Conference must start after %s", minStartDateTime));
        }

        if (endDateTime.isBefore(startDateTime)) {
            throw new ValidationException(String.format("Conference end dateTime %s is before start dateTime %s", endDateTime, startDateTime));
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
}
