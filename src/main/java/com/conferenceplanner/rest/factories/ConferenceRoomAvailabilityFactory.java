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


    public ConferenceRoomAvailability create(com.conferenceplanner.core.domain.ConferenceRoomAvailability conferenceRoomAvailability) {

        ConferenceRoomAvailability restDomainConferenceRoomAvailability = new ConferenceRoomAvailability();
        List<com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem> restDomainAvailabilityItems = new ArrayList<>();

        List<ConferenceRoomAvailabilityItem> availabilityItems = conferenceRoomAvailability.getAvailabilityItems();
        for (ConferenceRoomAvailabilityItem item: availabilityItems) {
            com.conferenceplanner.rest.domain.ConferenceRoomAvailabilityItem restDomainAvailabilityItem = create(item);
            restDomainAvailabilityItems.add(restDomainAvailabilityItem);
        }

        ConferenceRoom conferenceRoom = conferenceRoomAvailability.getConferenceRoom();

        com.conferenceplanner.rest.domain.ConferenceRoom restDomainConferenceRoom = conferenceRoomFactory.create(conferenceRoom);
        restDomainConferenceRoomAvailability.setConferenceRoom(restDomainConferenceRoom);
        restDomainConferenceRoomAvailability.setConferenceRoomAvailabilityItems(restDomainAvailabilityItems);
        return restDomainConferenceRoomAvailability;
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
