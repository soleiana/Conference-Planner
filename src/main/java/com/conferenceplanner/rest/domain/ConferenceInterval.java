package com.conferenceplanner.rest.domain;


import java.time.LocalDateTime;

public class ConferenceInterval {

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
}
