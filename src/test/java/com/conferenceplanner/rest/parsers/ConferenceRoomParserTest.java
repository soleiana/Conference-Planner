package com.conferenceplanner.rest.parsers;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ConferenceRoomParserTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_throws_ParserException_if_too_short_location() {
        String nameString = "";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid location length");
        String locationString = "aaa  ";
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_too_long_location() {
        String nameString = "";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid location length");
        String locationString = "aaa  aaa  aaa  aaa  aaa  aaa";
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_invalid_word_format() {
        String nameString = "";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid location format");
        String locationString = "a/a  aa9  aaa  aaa  aaa";
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_empty_name() {
        String nameString = "";
        String locationString = "A/A  aaa  aaa";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid name length");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_white_space_character_name() {
        String nameString = " ";
        String locationString = "A/A  aaa  aaa";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid name length");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_too_short_name() {
        String nameString = "A/A aaa conference";
        String locationString = "A/A  aaa  aaa";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid name length");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_too_long_name() {
        String nameString = "A/A aaa aaa aaa conference";
        String locationString = "A/A  aaa  aaa";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid name length");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_empty_location(){
        String nameString = "A/A aaa aaa  conference";
        String locationString = "";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid location length");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_white_space_character_location(){
        String nameString = "A/A aaa aaa  conference";
        String locationString = " ";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid location length");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_name_does_not_match_location(){
        String nameString = "A/A aaa aaa  conference";
        String locationString = "A/A  aaa  aab";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid name or location");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_throws_ParserException_if_name_does_not_end_with_conference(){
        String nameString = "A/A aaa aaa  conferenc";
        String locationString = "A/A  aaa  aaa";
        expectedException.expect(ParserException.class);
        expectedException.expectMessage("Invalid name format");
        ConferenceRoomParser.parse(locationString, nameString);
    }

    @Test
    public void test_parse_if_in_lower_case(){
        String nameString = "a/a aaa aaa  conference";
        String locationString = "a/a  aaa  aaa";
        boolean result = ConferenceRoomParser.parse(locationString, nameString);
        assertTrue(result);

        nameString = "aaa aaa  conference";
        locationString = "  aaa   aaa";
        result = ConferenceRoomParser.parse(locationString, nameString);
        assertTrue(result);
    }

    @Test
    public void test_parse_if_in_upper_case(){
        String nameString = "A/A AAA AAA  CONFERENCE";
        String locationString = "A/A  AAA  AAA";
        boolean result = ConferenceRoomParser.parse(locationString, nameString);
        assertTrue(result);

        nameString = "AAA AAA  CONFERENCE";
        locationString = "  AAA  AAA";
        result = ConferenceRoomParser.parse(locationString, nameString);
        assertTrue(result);
    }

}