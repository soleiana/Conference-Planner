package com.conferenceplanner.rest.domain;


import com.conferenceplanner.rest.parsers.ConferenceParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConferenceInterval {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(ConferenceParser.CONFERENCE_DATE_TIME_FORMAT_PATTERN);

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public ConferenceInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getFormattedStartDateTimeString() {
        return startDateTime.format(DATE_TIME_FORMATTER);
    }

    public String getFormattedEndDateTimeString() {
        return endDateTime.format(DATE_TIME_FORMATTER);
    }

    public static String getFormattedDateTimeString(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

}
