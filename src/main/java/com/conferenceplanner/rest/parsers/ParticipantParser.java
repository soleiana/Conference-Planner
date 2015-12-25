package com.conferenceplanner.rest.parsers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;


public class ParticipantParser {

    public static final String BIRTH_DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern(BIRTH_DATE_FORMAT_PATTERN);
    private static final int MIN_SYMBOLS_IN_PARTICIPANT_NAME = 2;
    private static final int MAX_SYMBOLS_IN_PARTICIPANT_NAME = 100;
    private static final int MIN_SYMBOLS_IN_PASSPORT_NUMBER = 6;
    private static final int MAX_SYMBOLS_IN_PASSPORT_NUMBER = 15;

    private static final String NAME = "[a-z]+";
    private static final String PASSPORT_NUMBER = "[a-z]([0-9]|[a-z])+";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME);
    private static final Pattern PASSPORT_NUMBER_PATTERN = Pattern.compile(PASSPORT_NUMBER);


    public static void parseName(String nameString) {
        String name = nameString.trim().toLowerCase();
        if (name.length() < MIN_SYMBOLS_IN_PARTICIPANT_NAME || name.length() > MAX_SYMBOLS_IN_PARTICIPANT_NAME) {
            throw new ParserException("Invalid name length");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new ParserException("Invalid name format");
        }
    }

    public static void parsePassportNr(String passportNrString) {
        String passportNr = passportNrString.trim().toLowerCase();
        if (passportNr.length() < MIN_SYMBOLS_IN_PASSPORT_NUMBER || passportNr.length() > MAX_SYMBOLS_IN_PASSPORT_NUMBER) {
            throw new ParserException("Invalid passport number length");
        }
        if (!PASSPORT_NUMBER_PATTERN.matcher(passportNr).matches()) {
            throw new ParserException("Invalid passport number format");
        }
    }

    public static LocalDate parseBirthDate(String birthDateString) {
        try {
            return LocalDate.parse(birthDateString.trim(), BIRTH_DATE_FORMATTER);

        } catch (DateTimeParseException ex) {
            throw new ParserException("Invalid birth date format");
        }
    }
}
