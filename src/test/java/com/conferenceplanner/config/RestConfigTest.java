package com.conferenceplanner.config;

import com.conferenceplanner.core.services.ConferenceService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static junit.framework.TestCase.assertNotNull;


public class RestConfigTest {

    @Test
    public void testCoreConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RestConfig.class);
        ConferenceService conferenceService = context.getBean(ConferenceService.class);
        assertNotNull(conferenceService);
    }
}
