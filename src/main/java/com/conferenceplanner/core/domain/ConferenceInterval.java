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
        if (dateTime.isAfter(startDateTime) && dateTime.isBefore(endDateTime)) {
            return true;
        }
        return false;
    }

    public static LocalDateTime getActualEndDateTime(LocalDateTime endDateTime){
        LocalDateTime actualDateTime = endDateTime.plusMinutes(MIN_INTERVAL_BETWEEN_CONFERENCES_IN_MINUTES);
        return actualDateTime;
    }
}
