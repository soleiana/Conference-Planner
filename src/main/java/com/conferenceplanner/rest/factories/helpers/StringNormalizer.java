package com.conferenceplanner.rest.factories.helpers;


public class StringNormalizer {

    public static String createNormalizedConferenceRoomLocation(String locationString) {

        if (locationString == null || locationString.isEmpty()) {
            throw new IllegalArgumentException("Can not normalize null or empty string!");
        }

        String normalizedString = "";
        String[] words = locationString.toLowerCase().split("\\s+");

        normalizedString += words[0].toUpperCase();

        for (int i = 1; i < words.length; i++) {
            normalizedString += " ";
            normalizedString += capitalize(words[i]);
        }
        return normalizedString;
    }

    public static String createNormalizedConferenceRoomName(String nameString) {

        if (nameString == null || nameString.isEmpty()) {
            throw new IllegalArgumentException("Can not normalize null or empty string!");
        }

        String normalizedString = "";
        String[] words = nameString.toLowerCase().split("\\s+");

        normalizedString += words[0].toUpperCase();

        for (int i = 1; i < words.length; i++) {
            normalizedString += " ";
            if (i != words.length - 1) {
                normalizedString += capitalize(words[i]);
            } else {
                normalizedString += words[i];
            }
        }

        return normalizedString;
    }

    private static String capitalize(String str) {
        String capitalizedString;

        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Can not capitalize null or empty string!");
        }
        char firstCharacter = str.charAt(0);
        capitalizedString = str.replace(firstCharacter, Character.toUpperCase(firstCharacter));
        return capitalizedString;
    }
}
