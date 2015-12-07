package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.ConferenceRoomAvailability;
import org.springframework.stereotype.Component;


@Component
public class ConferenceRoomAvailabilityRepository extends SessionProvider {

    public void create(ConferenceRoomAvailability conferenceRoomAvailability) {
        getCurrentSession().saveOrUpdate(conferenceRoomAvailability);
    }
}
