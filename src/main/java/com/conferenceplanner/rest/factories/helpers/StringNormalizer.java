package com.conferenceplanner.rest.factories.helpers;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringNormalizer {

    private static final String CONFERENCE_ROOM_LOCATION_FIRST_WORD_PATTERN = "[a-z]/[a-z]";
    private static final String CONFERENCE_ROOM_NAME_SUFFIX = "conference";

    public static String createNormalizedConferenceRoomLocation(String locationString) {
        List<String> words = Arrays.asList(locationString.toLowerCase().trim().split("\\s+"));

        String head = normalizeFirstWord(words.get(0));
        String tail = words.stream()
                .skip(1)
                .map(StringNormalizer::capitalize)
                .collect(Collectors.joining(" "));

        return head.concat(" ").concat(tail);
    }

    public static String createNormalizedConferenceRoomName(String nameString) {
        List<String> words = Arrays.asList(nameString.toLowerCase().trim().split("\\s+"));

        String head = normalizeFirstWord(words.get(0));

        String tail = words.stream()
                .limit(words.size()-1)
                .skip(1)
                .map(StringNormalizer::capitalize)
                .collect(Collectors.joining(" "));

        return head.concat(" ").concat(tail).concat(" ").concat(CONFERENCE_ROOM_NAME_SUFFIX);
    }

    public static String createNormalizedConferenceName(String nameString) {
        return nameString.trim().replaceAll("\\s+", " ");
    }

    public static String createNormalizedParticipantName(String nameString) {
        return capitalize(nameString.trim().toLowerCase());
    }

    public static String createNormalizedParticipantSurname(String surnameString) {
        return createNormalizedParticipantName(surnameString);
    }

    public static String createNormalizedParticipantPassportNr(String passportNrString) {
        return passportNrString.trim().toUpperCase();
    }

    private static String normalizeFirstWord(String word) {
        if (word.matches(CONFERENCE_ROOM_LOCATION_FIRST_WORD_PATTERN)) {
            return word.toUpperCase();
        } else {
            return capitalize(word);
        }
    }

    private static String capitalize(String str) {
        String firstCharacter = str.substring(0, 1).toUpperCase();
        return firstCharacter.concat(str.substring(1));
    }
}
