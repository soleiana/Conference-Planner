package com.conferenceplanner;
/**
 * Created by Ann on 07/09/14.
 */

import com.conferenceplanner.config.RestConfig;
import com.conferenceplanner.config.DatasourceConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, DatasourceConfig.class})
@Rollback(false)
@org.springframework.transaction.annotation.Transactional(transactionManager = "transactionManager")
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Ignore
public class SpringContextTest
{}
