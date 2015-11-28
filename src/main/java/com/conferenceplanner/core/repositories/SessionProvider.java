package com.conferenceplanner.core.repositories;

/**
 * Created by Ann on 09/09/14.
 */
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SessionProvider
{
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    protected Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }
}
