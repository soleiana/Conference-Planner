package com.conferenceplanner.rest.fixtures;

import com.conferenceplanner.rest.domain.Conference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConferenceFixture {

    public static final String CONFERENCE_DATE_TIME_FORMAT_PATTERN = "dd/MM/yyyy HH:mm";
    private static final DateTimeFormatter CONFERENCE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CONFERENCE_DATE_TIME_FORMAT_PATTERN);
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime START_DATE_TIME = NOW.plusDays(3);
    private static final LocalDateTime END_DATE_TIME = START_DATE_TIME.plusDays(1);
    private static final String NAME = "Devoxx 2016";
    private static final String ANOTHER_NAME = "JavaOne";

    public static Conference createConference() {
       return new Conference(NAME, getFormattedDateTime(START_DATE_TIME), getFormattedDateTime(END_DATE_TIME));
    }

    public static Conference createAnotherConference() {
        return new Conference(ANOTHER_NAME, getFormattedDateTime(START_DATE_TIME.plusDays(6)), getFormattedDateTime(END_DATE_TIME.plusDays(7)));
    }

    public static Conference createConference(List<Integer> conferenceRoomIds) {
        Conference conference = createConference();
        conference.setConferenceRoomIds(conferenceRoomIds);
        return conference;
    }

    public static Conference createAnotherConference(List<Integer> conferenceRoomIds) {
        Conference conference = new Conference(ANOTHER_NAME, getFormattedDateTime(START_DATE_TIME.plusDays(6)), getFormattedDateTime(END_DATE_TIME.plusDays(7)));
        conference.setConferenceRoomIds(conferenceRoomIds);
        return conference;
    }

    public static List<Conference> createConferences(int number) {
        return Stream.iterate(1, i -> i++)
                .limit(number)
                .map(i -> new Conference())
                .collect(Collectors.toList());
    }


    public static String getStartDateTime() {
        return NOW.plusDays(3).format(CONFERENCE_DATE_TIME_FORMATTER);
    }

    public static String getEndDateTime() {
        return NOW.plusDays(5).format(CONFERENCE_DATE_TIME_FORMATTER);
    }

    private static String getFormattedDateTime(LocalDateTime dateTime) {
        return dateTime.format(CONFERENCE_DATE_TIME_FORMATTER);
    }
}
