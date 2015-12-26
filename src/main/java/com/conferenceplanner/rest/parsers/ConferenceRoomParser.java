package com.conferenceplanner.rest.parsers;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        List<String> locationWords = Arrays.asList(locationString.toLowerCase().trim().split("\\s+"));
        List<String> nameWords = Arrays.asList(nameString.toLowerCase().trim().split("\\s+"));

        if (!nameWords.get(nameWords.size()-1).equals(CONFERENCE_ROOM_NAME_SUFFIX)) {
            throw new ParserException("Invalid name format");
        }

        if (!match(locationWords, nameWords)) {
            throw new ParserException("Invalid name or location");
        }
    }

    private static boolean match(List<String> locationWords, List<String> nameWords) {
        String location = locationWords.stream()
                .collect(Collectors.joining(" "));
        String name = nameWords.stream()
                .limit(nameWords.size() - 1)
                .collect(Collectors.joining(" "));
        return location.equals(name);
    }

}
