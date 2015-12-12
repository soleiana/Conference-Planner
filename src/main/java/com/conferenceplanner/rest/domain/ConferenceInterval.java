package com.conferenceplanner.rest.domain;


import com.conferenceplanner.rest.parsers.ConferenceParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConferenceInterval {

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern(ConferenceParser.DATE_TIME_FORMAT_PATTERN);

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

    public String getFormattedStartDateTime() {
        return startDateTime.format(dateTimeFormatter);
    }

    public String getFormattedEndDateTime() {
        return endDateTime.format(dateTimeFormatter);
    }
}
