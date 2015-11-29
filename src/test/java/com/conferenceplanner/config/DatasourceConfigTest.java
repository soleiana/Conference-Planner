package com.conferenceplanner.config;

/**
 * Created by Ann on 07/09/14.
 */

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static junit.framework.TestCase.assertNotNull;


public class DatasourceConfigTest
{

    @Test
    public void testDatasourceConfig()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(DatasourceConfig.class);

     //   BankParamsRepository bankParamsRepository = context.getBean(BankParamsRepository.class);

     //   assertNotNull(bankParamsRepository);
    }

}
