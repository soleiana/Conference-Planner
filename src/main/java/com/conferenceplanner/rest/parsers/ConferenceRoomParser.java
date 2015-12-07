package com.conferenceplanner.rest.parsers;


public class ConferenceRoomParser {

    private static final int MIN_WORDS_IN_LOCATION = 2;
    private static final int MAX_WORDS_IN_LOCATION = 5;
    private static final String LOCATION_FIRST_WORD_PATTERN = "[a-z]/[a-z]";
    private static final String LOCATION_WORD_PATTERN = "[a-z]+";


    public static boolean parse(String locationString, String nameString) {
        parseLocation(locationString);
        parseName(locationString, nameString);
        return true;
    }

    private static void parseLocation(String locationString) {
        String[] location = locationString.toLowerCase().split("\\s+");

        if (location.length < MIN_WORDS_IN_LOCATION || location.length > MAX_WORDS_IN_LOCATION) {
            throw new ParserException("Invalid location length");
        }
        if (!location[0].matches(LOCATION_FIRST_WORD_PATTERN)) {
            throw new ParserException("Invalid location format");
        }

        for (int i = 1; i < location.length; i++) {
            if (!location[i].matches(LOCATION_WORD_PATTERN))
                throw new ParserException("Invalid location format");
        }
    }

    private static void parseName(String locationString, String nameString) {
        String[] location = locationString.toLowerCase().split("\\s+");
        String[] name = nameString.toLowerCase().split("\\s+");

        if (name.length != location.length + 1) {
            throw new ParserException("Invalid name length");
        }
        for (int i = 0; i < location.length; i++) {
            if (!name[i].equals(location[i])) {
                throw new ParserException("Invalid name or location");
            }
        }
        if (!name[name.length-1].equals("conference")) {
            throw new ParserException("Invalid name format");
        }
    }
}
