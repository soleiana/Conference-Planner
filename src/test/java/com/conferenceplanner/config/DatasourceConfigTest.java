package com.conferenceplanner.config;


import com.conferenceplanner.core.repositories.ConferenceRoomRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static junit.framework.TestCase.assertNotNull;


public class DatasourceConfigTest {

    @Test
    public void testDatasourceConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DatasourceConfig.class);
        ConferenceRoomRepository conferenceRoomRepository = context.getBean(ConferenceRoomRepository.class);
        assertNotNull(conferenceRoomRepository);
    }
}
