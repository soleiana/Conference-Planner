package com.conferenceplanner.rest.factories;


import com.conferenceplanner.core.domain.ConferenceRoom;
import com.conferenceplanner.core.domain.ConferenceRoomAvailabilityItem;
import com.conferenceplanner.rest.domain.Conference;
import com.conferenceplanner.rest.domain.ConferenceRoomAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConferenceRoomAvailabilityFactory {

    @Autowired
    private ConferenceRoomFactory conferenceRoomFactory;

    @Autowired
    private ConferenceFactory conferenceFactory;


    public ConferenceRoomAvailability create(List<ConferenceRoomAvailabilityItem> conferenceRoomAvailabilityItems,
                                             ConferenceRoom conferenceRoom) {

        ConferenceRoomAvailability conferenceRoomAvailability = new ConferenceRoomAvailability();

        List<com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem> restDomainAvailabilityItems =
                new ArrayList<>();

        for (ConferenceRoomAvailabilityItem item: conferenceRoomAvailabilityItems) {
            com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem restDomainAvailabilityItem = create(item);
            restDomainAvailabilityItems.add(restDomainAvailabilityItem);
        }

        com.conferenceplanner.rest.domain.ConferenceRoom restDomainConferenceRoom = conferenceRoomFactory.create(conferenceRoom);
        conferenceRoomAvailability.setConferenceRoom(restDomainConferenceRoom);
        conferenceRoomAvailability.setConferenceRoomAvailabilityItems(restDomainAvailabilityItems);
        return conferenceRoomAvailability;

    }

    private com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem create(ConferenceRoomAvailabilityItem conferenceRoomAvailabilityItem) {

        com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem restDomainAvailabilityItem =
                new com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem();

        restDomainAvailabilityItem.setAvailableSeats(conferenceRoomAvailabilityItem.getAvailableSeats());
        Conference restDomainConference = conferenceFactory.create(conferenceRoomAvailabilityItem.getConference());
        restDomainAvailabilityItem.setConference(restDomainConference);

        return restDomainAvailabilityItem;
    }
}
