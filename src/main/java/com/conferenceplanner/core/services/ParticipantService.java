package com.conferenceplanner.core.services;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.domain.ConferenceParticipants;
import com.conferenceplanner.core.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class ParticipantService {

    @Autowired
    private ParticipantServiceAssistant serviceAssistant;

    @Autowired
    private ConferenceService conferenceService;

}
