package com.conferenceplanner.rest.parsers;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConferenceRoomParser {

    private static final int MIN_WORDS_IN_LOCATION = 2;
    private static final int MAX_WORDS_IN_LOCATION = 5;
    private static final String LOCATION_PATTERN = "([a-z]/[a-z]\\s+)*([a-z]+\\s+)+([a-z]+)*";

    public static boolean parse(String locationString, String nameString) {
        parseLocation(locationString);
        parseName(locationString, nameString);
        return true;
    }

    private static void parseLocation(String locationString) {

        String locationStringToParse = locationString.toLowerCase().trim();

        String[] location = locationStringToParse.split("\\s+");

        if (location.length < MIN_WORDS_IN_LOCATION || location.length > MAX_WORDS_IN_LOCATION) {
            throw new ParserException("Invalid location length");
        }

        Pattern pattern = Pattern.compile(LOCATION_PATTERN);
        Matcher matcher = pattern.matcher(locationStringToParse);

        if (!matcher.matches()) {
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
        if (!name[name.length-1].equals("conference")) {
            throw new ParserException("Invalid name format");
        }
    }
}
