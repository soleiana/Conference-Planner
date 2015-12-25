package com.conferenceplanner.rest.parsers;


import java.util.regex.Pattern;

public class ConferenceRoomParser {

    private static final int MIN_WORDS_IN_CONFERENCE_ROOM_LOCATION = 2;
    private static final int MAX_WORDS_IN_CONFERENCE_ROOM_LOCATION = 5;
    private static final int MIN_SYMBOLS_IN_CONFERENCE_ROOM_LOCATION = 5;
    private static final int MAX_SYMBOLS_IN_CONFERENCE_ROOM_LOCATION = 100;
    private static final String CONFERENCE_ROOM_LOCATION = "([a-z]/[a-z]\\s+)*([a-z]+\\s+)*([a-z]+)*";
    private static final Pattern CONFERENCE_ROOM_LOCATION_PATTERN = Pattern.compile(CONFERENCE_ROOM_LOCATION);
    private static final String CONFERENCE_ROOM_NAME_SUFFIX = "conference";

    public static boolean parse(String locationString, String nameString) {
        parseLocation(locationString);
        parseName(locationString, nameString);
        return true;
    }

    private static void parseLocation(String locationString) {
        String locationStringToParse = locationString.toLowerCase().trim();
        String[] location = locationStringToParse.split("\\s+");

        if (locationStringToParse.length() < MIN_SYMBOLS_IN_CONFERENCE_ROOM_LOCATION || locationStringToParse.length() > MAX_SYMBOLS_IN_CONFERENCE_ROOM_LOCATION) {
            throw new ParserException("Invalid location length");
        }

        if (location.length < MIN_WORDS_IN_CONFERENCE_ROOM_LOCATION || location.length > MAX_WORDS_IN_CONFERENCE_ROOM_LOCATION) {
            throw new ParserException("Invalid location length");
        }

        if (!CONFERENCE_ROOM_LOCATION_PATTERN.matcher(locationStringToParse).matches()) {
            throw new ParserException("Invalid location format");
        }
    }

    private static void parseName(String locationString, String nameString) {
        String[] location = locationString.toLowerCase().trim().split("\\s+");
        String[] name = nameString.toLowerCase().trim().split("\\s+");

        if (name.length != location.length + 1) {
            throw new ParserException("Invalid name length");
        }
        for (int i = 0; i < location.length; i++) {
            if (!name[i].equals(location[i])) {
                throw new ParserException("Invalid name or location");
            }
        }
        if (!name[name.length-1].equals(CONFERENCE_ROOM_NAME_SUFFIX)) {
            throw new ParserException("Invalid name format");
        }
    }
}
