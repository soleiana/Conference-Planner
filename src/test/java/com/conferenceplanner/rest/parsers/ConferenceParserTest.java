package com.conferenceplanner.rest.parsers;


import com.conferenceplanner.rest.domain.ConferenceInterval;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConferenceParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_throws_ParserException_if_invalid_start_date_time_format() {
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
    public void test_throws_ParserException_if_invalid_end_date_time_format() {
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
    public void test_parse_conference_interval() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String startDateTimeString = " 12/12/2015 12:20 ";
        for (String dateTimeString : geValidDateTimeStrings()) {

            ConferenceInterval interval = ConferenceParser.parse(startDateTimeString, dateTimeString);
            assertEquals("12/12/2015 12:20", interval.getStartDateTime().format(formatter));
            assertEquals(dateTimeString.trim(), interval.getEndDateTime().format(formatter));
        }

        String endDateTimeString = " 12/12/2015 12:20 ";
        for (String dateTimeString : geValidDateTimeStrings()) {

            ConferenceInterval interval = ConferenceParser.parse(dateTimeString, endDateTimeString);
            assertEquals("12/12/2015 12:20", interval.getEndDateTime().format(formatter));
            assertEquals(dateTimeString.trim(), interval.getStartDateTime().format(formatter));
        }
    }

    private List<String> geInvalidDateTimeStrings(){
        List<String> strings = new ArrayList<>();
        strings.add("a2/12/2015");
        strings.add("12/ 12/2015");
        strings.add("12/12/2015  12:20");
        strings.add("12/12/2015 a2:20");
        strings.add("32/12/2015 12:20");
        strings.add("12/12/2015 25:20");
        strings.add("12/12/2015 20:60");
        strings.add("");
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

}