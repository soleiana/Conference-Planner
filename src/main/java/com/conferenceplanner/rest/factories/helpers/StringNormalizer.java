package com.conferenceplanner.rest.factories.helpers;


public class StringNormalizer {

    private static final String FIRST_WORD_PATTERN = "[a-z]/[a-z]";

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
        return null;
    }

    public static String createNormalizedParticipantSurname(String surnameString) {
        return null;
    }

    public static String createNormalizedParticipantPassportNr(String passportNrString) {
        return null;
    }

    private static String normalizeFirstWord(String word) {
        if (word.matches(FIRST_WORD_PATTERN)) {
            return word.toUpperCase();
        } else {
            return capitalize(word);
        }
    }

    private static String capitalize(String str) {
        String capitalizedString;
        char firstCharacter = str.charAt(0);
        capitalizedString = str.replace(firstCharacter, Character.toUpperCase(firstCharacter));
        return capitalizedString;
    }
}
