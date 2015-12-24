package com.conferenceplanner;


import com.conferenceplanner.config.RestConfig;
import com.conferenceplanner.config.DatasourceConfig;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, DatasourceConfig.class})
@Rollback(true)
@Transactional(transactionManager = "transactionManager")
@Ignore
public class SpringContextTest
{}
