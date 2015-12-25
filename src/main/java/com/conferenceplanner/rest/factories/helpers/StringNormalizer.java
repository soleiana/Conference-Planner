package com.conferenceplanner.rest.factories.helpers;


public class StringNormalizer {

    private static final String CONFERENCE_ROOM_LOCATION_FIRST_WORD_PATTERN = "[a-z]/[a-z]";

    public static String createNormalizedConferenceRoomLocation(String locationString) {
        String normalizedString = "";
        String[] words = locationString.toLowerCase().trim().split("\\s+");

        normalizedString += normalizeFirstWord(words[0]);

        for (int i = 1; i < words.length; i++) {
            normalizedString += " " + capitalize(words[i]);
        }
        return normalizedString;
    }

    public static String createNormalizedConferenceRoomName(String nameString) {
        String normalizedString = "";
        String[] words = nameString.toLowerCase().trim().split("\\s+");

        normalizedString += normalizeFirstWord(words[0]);

        for (int i = 1; i < words.length - 1; i++) {
            normalizedString += " " + capitalize(words[i]);
        }
        normalizedString += " conference";
        return normalizedString;
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
        String firstCharacter = str.substring(0,1).toUpperCase();
        return firstCharacter.concat(str.substring(1));
    }
}
