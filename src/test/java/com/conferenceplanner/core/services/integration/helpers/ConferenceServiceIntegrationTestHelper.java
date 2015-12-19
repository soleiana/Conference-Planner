package com.conferenceplanner.core.services.integration.helpers;

import com.conferenceplanner.core.domain.Conference;
import com.conferenceplanner.core.services.ConferenceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Component
public class ConferenceServiceIntegrationTestHelper {

    @Autowired
    private ConferenceChecker conferenceChecker;

    private LocalDateTime now;


    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public void assertGetUpcomingConferencesResult(List<Conference> conferences) {
        for (Conference conference: conferences) {
            assertFalse(conference.isCancelled());
            assertTrue(conference.getStartDateTime().isAfter(now));
        }
    }

    public void assertGetAvailableConferencesResult(List<Conference> conferences) {
        for (Conference conference: conferences) {
            assertTrue(conferenceChecker.isAvailable(conference));
        }
    }
}
