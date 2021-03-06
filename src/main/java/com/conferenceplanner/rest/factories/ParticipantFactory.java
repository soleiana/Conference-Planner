package com.conferenceplanner.rest.factories;

import com.conferenceplanner.rest.domain.Participant;
import com.conferenceplanner.rest.factories.helpers.StringNormalizer;
import com.conferenceplanner.rest.parsers.ParticipantParser;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ParticipantFactory {

    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern(ParticipantParser.BIRTH_DATE_FORMAT_PATTERN);

    public Participant create(com.conferenceplanner.core.domain.Participant participant) {
        Participant restDomainParticipant = new Participant();
        restDomainParticipant.setId(participant.getId());
        restDomainParticipant.setName(participant.getName());
        restDomainParticipant.setSurname(participant.getSurname());
        restDomainParticipant.setBirthDate(participant.getBirthDate().format(BIRTH_DATE_FORMATTER));
        restDomainParticipant.setPassportNr(participant.getPassportNr());
        return restDomainParticipant;
    }

    public com.conferenceplanner.core.domain.Participant create(Participant participant) {
        String name = StringNormalizer.createNormalizedParticipantName(participant.getName());
        String surname = StringNormalizer.createNormalizedParticipantSurname(participant.getSurname());
        String passportNr = StringNormalizer.createNormalizedParticipantPassportNr(participant.getPassportNr());
        LocalDate birthDate = ParticipantParser.parseBirthDate(participant.getBirthDate());
        return new com.conferenceplanner.core.domain.Participant(name, surname, birthDate, passportNr);
    }
}
