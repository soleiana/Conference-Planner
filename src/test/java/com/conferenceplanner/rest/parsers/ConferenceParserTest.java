package com.conferenceplanner.rest.parsers;


import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.RestTestHelper;
import com.conferenceplanner.rest.domain.ConferenceInterval;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class ConferenceParserTest extends SpringContextTest{

    private static final int MIN_SYMBOLS_IN_CONFERENCE_NAME = 2;
    private static final int MAX_SYMBOLS_IN_CONFERENCE_NAME = 150;
    private static final DateTimeFormatter CONFERENCE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private RestTestHelper testHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_parse_throws_ParserException_if_invalid_start_date_time_format() {
        String endDateTimeString = "12/12/2015 12:20";
        for (String startDateTimeString: geInvalidDateTimeStrings()) {
           try {
               ConferenceParser.parse(startDateTimeString, endDateTimeString);
           } catch (ParserException ex) {
               assertEquals("Invalid start dateTime or end dateTime format", ex.getMessage());
               continue;
           }
           fail();
       }
    }

    @Test
    public void test_parse_throws_ParserException_if_invalid_end_date_time_format() {
        String startDateTimeString = "12/12/2015 12:20";
        for (String endDateTimeString: geInvalidDateTimeStrings()) {
            try {
                ConferenceParser.parse(startDateTimeString, endDateTimeString);
            } catch (ParserException ex) {
                assertEquals("Invalid start dateTime or end dateTime format", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_parse_conference_startDateTime() {
        String endDateTimeString = " 12/12/2015 12:20 ";
        for (String startDateTimeString : geValidDateTimeStrings()) {
            ConferenceInterval interval = ConferenceParser.parse(startDateTimeString, endDateTimeString);
            assertEquals(startDateTimeString.trim(), interval.getStartDateTime().format(CONFERENCE_DATE_TIME_FORMATTER));
        }
    }

    @Test
    public void test_parse_conference_endDateTime() {
        String startDateTimeString = " 12/12/2015 12:20 ";
        for (String endDateTimeString : geValidDateTimeStrings()) {
            ConferenceInterval interval = ConferenceParser.parse(startDateTimeString, endDateTimeString);
            assertEquals(endDateTimeString.trim(), interval.getEndDateTime().format(CONFERENCE_DATE_TIME_FORMATTER));
        }
    }

    @Test
    public void test_parseName_throws_ParserException() {
        for (String nameString: getInvalidLengthNameStrings()) {
            try {
                ConferenceParser.parseName(nameString);
            } catch (ParserException ex) {
                assertEquals("Invalid name length", ex.getMessage());
                continue;
            }
            fail();
        }
    }

    @Test
    public void test_parseName() {
        for (String nameString: getValidNameStrings()) {
            try {
                ConferenceParser.parseName(nameString);
            } catch (ParserException ex) {
               fail();
            }
        }
    }

    private List<String> geInvalidDateTimeStrings(){
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add(" ");
        strings.add("a2/12/2015");
        strings.add("12/ 12/2015");
        strings.add("12/12/2015  12:20");
        strings.add("12/12/2015 a2:20");
        strings.add("32/12/2015 12:20");
        strings.add("12/12/2015 25:20");
        strings.add("12/12/2015 20:60");
        return strings;
    }

    private List<String> geValidDateTimeStrings(){
        List<String> strings = new ArrayList<>();
        strings.add("12/12/2015 12:20");
        strings.add(" 12/12/2015 12:20  ");
        strings.add("31/12/2015 12:20");
        strings.add("01/12/2015 12:59");
        strings.add("12/01/2015 12:25");
        strings.add("12/01/2015 00:00");
        return strings;
    }

    private List<String> getInvalidLengthNameStrings() {
        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("  ");
        strings.add("a");
        strings.add("0");
        strings.add(" a ");
        strings.add(" % ");
        strings.add(testHelper.getTooLongNameString(MAX_SYMBOLS_IN_CONFERENCE_NAME));
        return strings;
    }

    private List<String> getValidNameStrings() {
        List<String> names = new ArrayList<>();
        names.add(testHelper.getValidNameString(MIN_SYMBOLS_IN_CONFERENCE_NAME));
        names.add(testHelper.getValidNameString(MAX_SYMBOLS_IN_CONFERENCE_NAME));
        names.add(testHelper.getValidNameString(MAX_SYMBOLS_IN_CONFERENCE_NAME - 1));
        return names;
    }
}