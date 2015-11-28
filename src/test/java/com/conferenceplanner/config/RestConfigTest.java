package com.conferenceplanner.config;

import com.conferenceplanner.core.services.QueryExecutor;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Ann on 08/09/14.
 */
public class RestConfigTest
{
    @Test
    public void testCoreConfig()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(RestConfig.class);

        QueryExecutor queryExecutor = context.getBean(QueryExecutor.class);

        assertNotNull(queryExecutor);
    }
}
