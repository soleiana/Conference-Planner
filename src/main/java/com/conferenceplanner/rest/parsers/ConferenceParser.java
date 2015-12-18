package com.conferenceplanner.rest.parsers;


import com.conferenceplanner.rest.domain.ConferenceInterval;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConferenceParser {

    public static final String DATE_TIME_FORMAT_PATTERN = "dd/MM/yyyy HH:mm";
    public static final int MIN_SYMBOLS_IN_CONFERENCE_NAME = 2;
    public static  final int MAX_SYMBOLS_IN_CONFERENCE_NAME =150;

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

    public static boolean parse(String nameString) {
        int length = nameString.trim().replaceAll("\\s+", " ").length();
        if (length < MIN_SYMBOLS_IN_CONFERENCE_NAME || length > MAX_SYMBOLS_IN_CONFERENCE_NAME) {
            throw new ParserException("Invalid name length");
        }
        return true;
    }
}
