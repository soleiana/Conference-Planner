package com.conferenceplanner.rest.factories;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import com.conferenceplanner.rest.factories.helpers.StringNormalizer;
import com.conferenceplanner.rest.parsers.ConferenceParser;
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

        String startDateTime = ConferenceInterval.getFormattedDateTimeString(conference.getStartDateTime());
        String endDateTime = ConferenceInterval.getFormattedDateTimeString(conference.getEndDateTime());

        restDomainConference.setName(conference.getName());
        restDomainConference.setStartDateTime(startDateTime);
        restDomainConference.setEndDateTime(endDateTime);

        return restDomainConference;
    }

    public Conference create(com.conferenceplanner.rest.domain.Conference conference) {
        String nameString = conference.getName();
        String startDateTime = conference.getStartDateTime();
        String endDateTime = conference.getEndDateTime();

        ConferenceInterval interval = ConferenceParser.parse(startDateTime, endDateTime);
        String name = StringNormalizer.createNormalizedConferenceName(nameString);
        return new Conference(name, interval.getStartDateTime(), interval.getEndDateTime(), false);
    }
}
