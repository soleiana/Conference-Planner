package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ConferenceFactory {

    public Conference create(ConferenceInterval interval) {
        LocalDateTime startDateTime = interval.getStartDateTime();
        LocalDateTime endDateTime = interval.getEndDateTime();

        return new Conference(startDateTime, endDateTime);
    }

    public com.conferenceplanner.rest.domain.Conference create(Conference conference) {
        com.conferenceplanner.rest.domain.Conference restDomainConference = new com.conferenceplanner.rest.domain.Conference();

        String startDateTime = ConferenceInterval.getFormattedStartDateTime(conference.getStartDateTime());
        String endDateTime = ConferenceInterval.getFormattedEndDateTime(conference.getEndDateTime());

        restDomainConference.setName(conference.getName());
        restDomainConference.setStartDateTime(startDateTime);
        restDomainConference.setEndDateTime(endDateTime);

        return restDomainConference;
    }
}
