package com.conferenceplanner.core.fixtures;

import com.conferenceplanner.core.domain.Conference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConferenceFixture {

    private static final LocalDateTime PLANNED_START = LocalDateTime.now().plusDays(2);
    private static final LocalDateTime PLANNED_END = PLANNED_START.plusHours(2);
    private static final LocalDateTime START_IN_PAST = LocalDateTime.now().minusHours(1);

    public static Conference createConference() {
        return new Conference(PLANNED_START, PLANNED_END);
    }

    public static Conference cloneConference(Conference conference) {
        return new Conference(conference.getName(), conference.getStartDateTime(), conference.getEndDateTime(), conference.isCancelled());
    }

    public static Conference createOngoingConference() {
        LocalDateTime endDateTime = START_IN_PAST.plusHours(5);
        return new Conference("name", START_IN_PAST, endDateTime, false);
    }

    public static Conference createCancelledConference() {
        return new Conference("name", PLANNED_START, PLANNED_END, true);
    }

    public static Conference createUpcomingConference() {
        return new Conference("name", PLANNED_START, PLANNED_END, false);
    }

    public static Conference createUpcomingConference(String name) {
        return new Conference(name, PLANNED_START, PLANNED_END, false);
    }

    public static List<Conference> createCancelledConferences() {
        List<Conference> conferences = new ArrayList<>();
        LocalDateTime start = PLANNED_START.plusHours(1);
        for (Integer i = 1; i <= 3; i++) {
            LocalDateTime end = start.plusHours(1);
            Conference conference = new Conference("name" + i.toString(), start, end, false);
            conference.setCancelled(true);
            conferences.add(conference);
            start = end.plusHours(1);
        }
        return conferences;
    }

    public static List<Conference> createUpcomingConferences() {
        List<Conference> conferences = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        for (Integer i = 1; i <= 3; i++) {
            LocalDateTime end = start.plusHours(1);
            Conference conference = new Conference("name" + i.toString(), start, end, false);
            conferences.add(conference);
            start = end.plusHours(1);
        }
        return conferences;
    }

    public static List<Conference> createNonOverlappingConferences() {
        List<Conference> conferences = new ArrayList<>();

        LocalDateTime start1 = PLANNED_START.minusHours(3);
        LocalDateTime end1 = PLANNED_START.minusMinutes(31);
        Conference conference1 = new Conference("name1", start1, end1, false);
        conferences.add(conference1);

        LocalDateTime start2 = PLANNED_END.plusMinutes(31);
        LocalDateTime end2 = start2.plusHours(2);
        Conference conference2 = new Conference("name2", start2, end2, false);
        conferences.add(conference2);
        return conferences;
    }

    public static List<Conference> createMixedConferences() {
        List<Conference> conferences = new ArrayList<>();

        LocalDateTime start1 = PLANNED_START.minusHours(3);
        LocalDateTime end1 = PLANNED_START.minusMinutes(31);
        Conference nonOverlappingConference = new Conference("name1", start1, end1, false);
        conferences.add(nonOverlappingConference);

        LocalDateTime start2 = PLANNED_END.plusMinutes(29);
        LocalDateTime end2 = start2.plusHours(2);
        Conference overlappingConference = new Conference("name2", start2, end2, false);
        conferences.add(overlappingConference);
        return conferences;
    }

}


