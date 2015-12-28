package com.conferenceplanner.rest.parsers;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.RestTestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParticipantParserTest extends SpringContextTest {

    private static final int MAX_SYMBOLS_IN_PARTICIPANT_NAME = 100;
    private static final int MIN_SYMBOLS_IN_PARTICIPANT_NAME = 2;

    @Autowired
    private RestTestHelper testHelper;


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_parseName() {
        for (String nameString: getValidNameStrings()) {
            try {
                ParticipantParser.parseName(nameString);
            } catch (ParserException ex) {
                fail();
            }
        }
    }

    @Test
    public void test_parseName_throws_ParserException_if_invalid_name_length() {
        for (String nameString: getInvalidLengthNameStrings()) {
            try {
                ParticipantParser.parseName(nameString);
            } catch (ParserException ex) {
                assertEquals("Invalid name length", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_parseName_throws_ParserException_if_invalid_name_format() {
        for (String nameString: getInvalidFormatNameStrings()) {
            try {
                ParticipantParser.parseName(nameString);
            } catch (ParserException ex) {
                assertEquals("Invalid name format", ex.getMessage());
                continue;
            }
            fail();
        }
    }


    @Test
    public void test_parseBirthDate_throws_ParserException_if_invalid_birthDate_format() {
        for (String birthDateString: geInvalidDateStrings()) {
            try {
                ParticipantParser.parseBirthDate(birthDateString);
            } catch (ParserException ex) {
                assertEquals("Invalid birth date format", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_parseBirthDate() {
        for (String birthDateString: geValidDateStrings()) {
            try {
                ParticipantParser.parseBirthDate(birthDateString);
            } catch (ParserException ex) {
               fail();
            }
        }
    }

    @Test
    public void test_parsePassportNr_throws_ParserException_if_invalid_passportNr_format() {
        for (String passportNrString: getInvalidFormatPassportNrStrings()) {
            try {
                ParticipantParser.parsePassportNr(passportNrString);
            } catch (ParserException ex) {
                assertEquals("Invalid passport number format", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_parsePassportNr_throws_ParserException_if_invalid_passportNr_length() {
        for (String passportNrString: getInvalidLengthPassportNrStrings()) {
            try {
                ParticipantParser.parsePassportNr(passportNrString);
            } catch (ParserException ex) {
                assertEquals("Invalid passport number length", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_parsePassportNr() {
        for (String passportNrString: getValidPassportNrStrings()) {
            try {
                ParticipantParser.parsePassportNr(passportNrString);
            } catch (ParserException ex) {
                fail();
            }
        }
    }


    private List<String> geInvalidDateStrings(){
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add(" ");
        strings.add("a2/12/2015");
        strings.add("12/ 12/2015");
        strings.add("12/12/2015 12:20");
        strings.add("32/12/2015");
        strings.add("12/13/2015");
        strings.add("01/01/999");
        return strings;
    }

    private List<String> geValidDateStrings(){
        List<String> strings = new ArrayList<>();
        strings.add("12/12/2015");
        strings.add(" 12/12/2015 ");
        strings.add("01/12/2015");
        strings.add("31/01/2015");
        strings.add("01/01/2015");
        strings.add("01/01/1970");
        return strings;
    }

    private List<String> getInvalidFormatPassportNrStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("001234");
        strings.add("0BCDEF");
        strings.add("A01-234");
        return strings;
    }

    private List<String> getInvalidLengthPassportNrStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add(" ");
        strings.add("A1234");
        strings.add("  A1234");
        strings.add("A11111A11111A111");
        return strings;
    }

    private List<String> getValidPassportNrStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("A01234");
        strings.add("ABCDEF");
        strings.add("  A01234 ");
        strings.add("A11111A11111A11");
        strings.add(" A11111A11111A11 ");
        return strings;
    }

    private List<String> getInvalidLengthNameStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add(" ");
        strings.add("a");
        strings.add(" a ");
        strings.add(testHelper.getTooLongNameString(MAX_SYMBOLS_IN_PARTICIPANT_NAME));
        return strings;
    }

    private List<String> getInvalidFormatNameStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("O'Reilly");
        strings.add("Jan-Eric");
        strings.add("Buddy9");
        strings.add("Sky walker");
        return strings;
    }

    private List<String> getValidNameStrings() {
        List<String> names = new ArrayList<>();
        names.add(testHelper.getValidNameString(MIN_SYMBOLS_IN_PARTICIPANT_NAME));
        names.add(testHelper.getValidNameString(MAX_SYMBOLS_IN_PARTICIPANT_NAME));
        names.add(testHelper.getValidNameString(MAX_SYMBOLS_IN_PARTICIPANT_NAME - 1));
        return names;
    }
}