package com.conferenceplanner.core.services.component.helpers;

import com.conferenceplanner.core.domain.Conference;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Component
public class ConferenceServiceTestHelper {

    private LocalDateTime now;


    public void setNow(LocalDateTime now) {
        this.now = now;
    }

    public void assertResult(List<Conference> conferences) {
        for (Conference conference: conferences) {
            assertFalse(conference.isCancelled());
            assertTrue(conference.getStartDateTime().isAfter(now));
        }
    }
}
