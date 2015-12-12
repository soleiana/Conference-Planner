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

}
