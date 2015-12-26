package com.conferenceplanner.rest.validators;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.rest.TestHelper;
import com.conferenceplanner.rest.domain.Participant;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class ParticipantValidatorTest extends SpringContextTest {

    private static final int MAX_SYMBOLS_IN_PARTICIPANT_NAME = 100;
    private static final int MIN_PARTICIPANT_AGE = 7;
    private static final int MAX_PARTICIPANT_AGE = 110;
    private static final String BIRTH_DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern(BIRTH_DATE_FORMAT_PATTERN);


    @Autowired
    private ParticipantValidator validator;

    @Autowired
    private TestHelper testHelper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void test_validateIds_throws_ValidationException_if_participant_id_is_null() {
        Participant participant = new Participant();
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Participant id is null");
        validator.validateIds(participant);
    }

    @Test
    public void test_validateIds_throws_ValidationException_if_conference_id_is_null() {
        Participant participant = new Participant();
        participant.setId(1);
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference id is null");
        validator.validateIds(participant);
    }

    @Test
    public void test_validateIds() {
        Participant participant = new Participant();
        participant.setId(1);
        participant.setConferenceId(1);
        try {
            validator.validateIds(participant);
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void test_validate_throws_ValidationException_if_too_short_participant_name() {
        Participant participant = new Participant();
        participant.setName("a");
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid name length");
        validator.validate(participant);
    }

    @Test
    public void test_validate_throws_ValidationException_if_too_long_participant_name() {
        Participant participant = new Participant();
        participant.setName(testHelper.getTooLongNameString(MAX_SYMBOLS_IN_PARTICIPANT_NAME));
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid name length");
        validator.validate(participant);
    }

    @Test
    public void test_validate_throws_ValidationException_if_invalid_participant_passportNr_format() {
        Participant participant = new Participant();
        participant.setName("Anna");
        participant.setSurname("Suleymanova");
        participant.setPassportNr("123456");
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid passport number format");
        validator.validate(participant);
    }

    @Test
    public void test_validate_throws_ValidationException_if_invalid_participant_birthDate_format() {
        Participant participant = new Participant();
        participant.setName("Anna");
        participant.setSurname("Suleymanova");
        participant.setPassportNr("A23456");
        participant.setBirthDate("00/03/1985");
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid birth date format");
        validator.validate(participant);
    }

    @Test
    public void test_validate_throws_ValidationException_if_too_young_participant() {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = now.minusYears(MIN_PARTICIPANT_AGE - 1);
        Participant participant = new Participant();
        participant.setName("Anna");
        participant.setSurname("Suleymanova");
        participant.setPassportNr("A23456");
        participant.setBirthDate(birthDate.format(BIRTH_DATE_FORMATTER));
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid birth date");
        validator.validate(participant);
    }

    @Test
    public void test_validate_throws_ValidationException_if_too_old_participant() {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = now.minusYears(MAX_PARTICIPANT_AGE + 1);
        Participant participant = new Participant();
        participant.setName("Anna");
        participant.setSurname("Suleymanova");
        participant.setPassportNr("A23456");
        participant.setBirthDate(birthDate.format(BIRTH_DATE_FORMATTER));
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Invalid birth date");
        validator.validate(participant);
    }

    @Test
    public void test_validate_throws_ValidationException_if_conference_id_is_null() {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = now.minusYears(MAX_PARTICIPANT_AGE);
        Participant participant = new Participant();
        participant.setName("Anna");
        participant.setSurname("Suleymanova");
        participant.setPassportNr("A23456");
        participant.setBirthDate(birthDate.format(BIRTH_DATE_FORMATTER));
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Conference id is null");
        validator.validate(participant);
    }

    @Test
    public void test_validate_participant() {
        LocalDate now = LocalDate.now();
        LocalDate birthDate = now.minusYears(MAX_PARTICIPANT_AGE);
        Participant participant = new Participant();
        participant.setName("Anna");
        participant.setSurname("Suleymanova");
        participant.setPassportNr("A23456");
        participant.setBirthDate(birthDate.format(BIRTH_DATE_FORMATTER));
        participant.setConferenceId(1);
        try {
            validator.validate(participant);
        } catch (Exception ex) {
            fail();
        }
    }

}