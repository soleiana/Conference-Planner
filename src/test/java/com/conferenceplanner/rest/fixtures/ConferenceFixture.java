package com.conferenceplanner.rest.fixtures;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConferenceFixture {

    public static final String CONFERENCE_DATE_TIME_FORMAT_PATTERN = "dd/MM/yyyy HH:mm";
    private static final DateTimeFormatter CONFERENCE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CONFERENCE_DATE_TIME_FORMAT_PATTERN);
    private static final LocalDateTime NOW = LocalDateTime.now();


    public static String getStartDateTime() {
        return NOW.plusDays(3).format(CONFERENCE_DATE_TIME_FORMATTER);
    }

    public static String getEndDateTime() {
        return NOW.plusDays(5).format(CONFERENCE_DATE_TIME_FORMATTER);
    }
}
