package com.conferenceplanner;


import com.conferenceplanner.config.RestConfig;
import com.conferenceplanner.config.DatasourceConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, DatasourceConfig.class})
@Rollback(false)
@org.springframework.transaction.annotation.Transactional(transactionManager = "transactionManager")
@Ignore
public class SpringContextTest
{}
