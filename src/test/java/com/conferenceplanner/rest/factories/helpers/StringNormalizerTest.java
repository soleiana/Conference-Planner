package com.conferenceplanner.rest.factories.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringNormalizerTest {

    @Test
    public void test_CreateNormalizedConferenceRoomLocation() {
        String locationString = "  m/s baLTic   queEn ";
        String normalisedString = StringNormalizer.createNormalizedConferenceRoomLocation(locationString);
        assertEquals("M/S Baltic Queen", normalisedString);
    }

    @Test
    public void test_CreateNormalizedConferenceRoomName() {
        String nameString = "  m/s baLTic  queEn CoNference ";
        String normalisedString = StringNormalizer.createNormalizedConferenceRoomName(nameString);
        assertEquals("M/S Baltic Queen conference", normalisedString);
    }

    @Test
    public void test_CreateNormalizedConferenceName() {
        String nameString = "  Jfokus    2016   ";
        String normalisedString = StringNormalizer.createNormalizedConferenceName(nameString);
        assertEquals("Jfokus 2016", normalisedString);
    }
}