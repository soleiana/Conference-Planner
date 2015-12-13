package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import org.springframework.stereotype.Component;


@Component
public class ConferenceRoomAvailabilityItemRepository extends SessionProvider {

    public void create(ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem) {
        getCurrentSession().saveOrUpdate(conferenceRoomAvailabilityItem);
    }
}
