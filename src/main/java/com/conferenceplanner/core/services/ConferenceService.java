package com.conferenceplanner.core.services;

import com.conferenceplanner.core.repositories.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;


}
