package com.conferenceplanner.rest.parsers;


import com.conferenceplanner.rest.domain.ConferenceInterval;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConferenceParser {

    public static final String DATE_TIME_FORMAT_PATTERN = "dd/MM/yyyy HH:mm";

    public static ConferenceInterval parse(String startDateTimeString, String endDateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString.trim(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString.trim(), formatter);
            return new ConferenceInterval(startDateTime, endDateTime);

        } catch (DateTimeParseException ex) {
            throw new ParserException("Invalid start dateTime or end dateTime format");
        }
    }
}
