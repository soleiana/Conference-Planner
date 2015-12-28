package com.conferenceplanner;

import com.conferenceplanner.core.domain.ConferenceRoom;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonTestHelper {

    public List<Integer> getConferenceRoomIds(List<ConferenceRoom> conferenceRooms) {
        return conferenceRooms.stream().map(ConferenceRoom::getId).collect(Collectors.toList());
    }

}
