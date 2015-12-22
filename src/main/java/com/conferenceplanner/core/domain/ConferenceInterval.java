package com.conferenceplanner.core.domain;


import java.time.LocalDateTime;

public class ConferenceInterval {

    private static final int MIN_INTERVAL_BETWEEN_CONFERENCES_IN_MINUTES = 30;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public ConferenceInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public boolean contains(LocalDateTime dateTime) {
        return dateTime.isAfter(startDateTime) && dateTime.isBefore(endDateTime);
    }

    public static LocalDateTime getActualStartDateTime(LocalDateTime startDateTime){
        return startDateTime.minusMinutes(MIN_INTERVAL_BETWEEN_CONFERENCES_IN_MINUTES);
    }

    public static LocalDateTime getActualEndDateTime(LocalDateTime endDateTime){
        return endDateTime.plusMinutes(MIN_INTERVAL_BETWEEN_CONFERENCES_IN_MINUTES);
    }
}
