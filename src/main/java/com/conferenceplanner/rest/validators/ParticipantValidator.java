package com.conferenceplanner.rest.validators;

import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.parsers.ParserException;
import com.conferenceplanner.rest.parsers.ParticipantParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ParticipantValidator {

    private static final int MIN_PARTICIPANT_AGE = 7;
    private static final int MAX_PARTICIPANT_AGE = 110;

    @Autowired
    private ConferenceValidator conferenceValidator;

    public void validateIds(Participant participant) {
        if (participant.getId() == null) {
            throw new ValidationException("Participant id is null");
        }
        conferenceValidator.validateId(participant.getConferenceId());
    }

    public void validate(Participant participant) {
        String nameString = participant.getName();
        String surnameString = participant.getSurname();
        String passportNrString = participant.getPassportNr();
        String birthDateString = participant.getPassportNr();

        validateName(nameString);
        validateName(surnameString);
        validatePassportNr(passportNrString);
        validateBirthDate(birthDateString);
        conferenceValidator.validateId(participant.getConferenceId());
    }

    private void validateName(String nameString) {
        if (nameString == null) {
            throw new ValidationException("Name is null");
        }
      try {
          ParticipantParser.parseName(nameString);
      } catch (ParserException ex) {
          throw new ValidationException(ex.getMessage());
      }
    }

    private void validateBirthDate(String birthDateString) {
        LocalDate birthDate;
        if (birthDateString == null) {
            throw new ValidationException("Birth date is null");
        }
        try {
            birthDate = ParticipantParser.parseBirthDate(birthDateString);
        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
        validateBirthDate(birthDate);
    }

    private void validatePassportNr(String passportNrString) {
        if (passportNrString == null) {
            throw new ValidationException("Passport Nr is null");
        }
        try {
            ParticipantParser.parsePassportNr(passportNrString);
        } catch (ParserException ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        int age = LocalDate.now().getYear() - birthDate.getYear();

        if (age < MIN_PARTICIPANT_AGE || age > MAX_PARTICIPANT_AGE) {
            throw new ValidationException("Invalid birth date");
        }
    }
}
