package com.conferenceplanner.rest.factories.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringNormalizerTest {

    @Test
    public void test_CreateNormalizedConferenceRoomLocation_with_right_slash() {
        String locationString = "  m/s baLTic   queEn ";
        String normalisedString = StringNormalizer.createNormalizedConferenceRoomLocation(locationString);
        assertEquals("M/S Baltic Queen", normalisedString);
    }

    @Test
    public void test_CreateNormalizedConferenceRoomLocation_without_right_slash() {
        String locationString = "  baLTic   queEn ";
        String normalisedString = StringNormalizer.createNormalizedConferenceRoomLocation(locationString);
        assertEquals("Baltic Queen", normalisedString);
    }

    @Test
    public void test_CreateNormalizedConferenceRoomName_with_right_slash() {
        String nameString = "  m/s baLTic  queEn CoNference ";
        String normalisedString = StringNormalizer.createNormalizedConferenceRoomName(nameString);
        assertEquals("M/S Baltic Queen conference", normalisedString);
    }

    @Test
    public void test_CreateNormalizedConferenceRoomName_without_right_slash() {
        String nameString = "  baLTic  queEn CoNference ";
        String normalisedString = StringNormalizer.createNormalizedConferenceRoomName(nameString);
        assertEquals("Baltic Queen conference", normalisedString);
    }

    @Test
    public void test_CreateNormalizedConferenceName() {
        String nameString = "  Jfokus    2016   ";
        String normalisedString = StringNormalizer.createNormalizedConferenceName(nameString);
        assertEquals("Jfokus 2016", normalisedString);
    }

    @Test
    public void test_createNormalizedParticipantName() {
        String nameString = " anNA ";
        String normalizedString = StringNormalizer.createNormalizedParticipantName(nameString);
        assertEquals("Anna", normalizedString);
    }

    @Test
    public void test_createNormalizedParticipantSurname() {
        String surnameString = " sULeyManova ";
        String normalizedString = StringNormalizer.createNormalizedParticipantSurname(surnameString);
        assertEquals("Suleymanova", normalizedString);
    }

    @Test
    public void test_createNormalizedParticipantPassportNr() {
        String  passportNrString = " af123456F ";
        String normalizedString = StringNormalizer.createNormalizedParticipantPassportNr(passportNrString);
        assertEquals("AF123456F", normalizedString);

    }
}