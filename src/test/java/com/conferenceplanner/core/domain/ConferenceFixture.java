package com.conferenceplanner.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConferenceFixture {

    private static final String NAME = "Devoxx";
    private static final String ANOTHER_NAME = "Jfokus";
    private static final LocalDateTime START_DATE_TIME_FROM_NOW_IN_FUTURE = LocalDateTime.now().plusHours(1L);
    private static final LocalDateTime START_DATE_TIME_FROM_NOW_IN_PAST = LocalDateTime.now().minusHours(1L);
    private static final LocalDateTime END_DATE_TIME_FROM_NOW = LocalDateTime.now().plusHours(5L);

    public static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2016, 10, 9, 9, 30);
    public static final LocalDateTime END_DATE_TIME = LocalDateTime.of(2016, 10, 13, 16, 30);

    public static Conference createConference(String name) {
        return new Conference(name, START_DATE_TIME, END_DATE_TIME, false);
    }

    public static List<Conference> createInputData() {
        List<Conference> conferences = new ArrayList<>();

        Conference conferenceInFuture1 = new Conference(NAME, START_DATE_TIME_FROM_NOW_IN_FUTURE, END_DATE_TIME_FROM_NOW, false);
        conferences.add(conferenceInFuture1);

        Conference conferenceInFuture2 = new Conference(ANOTHER_NAME, START_DATE_TIME_FROM_NOW_IN_FUTURE, END_DATE_TIME_FROM_NOW, false);
        conferences.add(conferenceInFuture2);

        Conference startedConference1 = new Conference(NAME, START_DATE_TIME_FROM_NOW_IN_PAST, END_DATE_TIME_FROM_NOW, false);
        conferences.add(startedConference1);

        Conference startedConference2 = new Conference(ANOTHER_NAME, START_DATE_TIME_FROM_NOW_IN_PAST, END_DATE_TIME_FROM_NOW, false);
        conferences.add(startedConference2);

        Conference cancelledConferenceInFuture1 = new Conference(NAME, START_DATE_TIME_FROM_NOW_IN_FUTURE, END_DATE_TIME_FROM_NOW, true);
        conferences.add(cancelledConferenceInFuture1);

        Conference cancelledConferenceInFuture2 = new Conference(ANOTHER_NAME, START_DATE_TIME_FROM_NOW_IN_FUTURE, END_DATE_TIME_FROM_NOW, true);
        conferences.add(cancelledConferenceInFuture2);

        Conference cancelledConferenceInPast1 = new Conference(NAME, START_DATE_TIME_FROM_NOW_IN_PAST, END_DATE_TIME_FROM_NOW, true);
        conferences.add(cancelledConferenceInPast1);

        Conference cancelledConferenceInPast2 = new Conference(ANOTHER_NAME, START_DATE_TIME_FROM_NOW_IN_PAST, END_DATE_TIME_FROM_NOW, true);
        conferences.add(cancelledConferenceInPast2);

        return conferences;
    }

    public static List<Conference> createExpectedData() {

        List<Conference> conferences = new ArrayList<>();

        Conference conferenceInFuture1 = new Conference(NAME, START_DATE_TIME_FROM_NOW_IN_FUTURE, END_DATE_TIME_FROM_NOW, false);
        conferences.add(conferenceInFuture1);

        Conference conferenceInFuture2 = new Conference(ANOTHER_NAME, START_DATE_TIME_FROM_NOW_IN_FUTURE, END_DATE_TIME_FROM_NOW, false);
        conferences.add(conferenceInFuture2);

        return conferences;
    }

}
