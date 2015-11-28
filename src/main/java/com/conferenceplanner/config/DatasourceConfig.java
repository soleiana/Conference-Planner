package com.conferenceplanner.config;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Ann on 07/09/14.
 */
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:datasource.properties" })
@ComponentScan(basePackages = {"com.conferenceplanner.core.domain", "com.conferenceplanner.core.repositories"})
public class DatasourceConfig
{
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource()
    {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean(name = "sessionFactory")
    public AnnotationSessionFactoryBean sessionFactory()
    {
        AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.conferenceplanner.core.domain" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean(name = "transactionManager")
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
    {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    Properties hibernateProperties()
    {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
                setProperty("database.password", env.getProperty("database.password"));
                setProperty("database.userName", env.getProperty("database.userName"));
                setProperty("database.jdbcUrl", env.getProperty("database.jdbcUrl"));
                setProperty("database.driverClass", env.getProperty("database.driverClass"));
            }
        };
    }
}
