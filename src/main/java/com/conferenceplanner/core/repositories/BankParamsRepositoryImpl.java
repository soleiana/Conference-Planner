package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.BankParams;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Component;


/**
 * Created by Ann on 07/09/14.
 */
@Component
public class BankParamsRepositoryImpl extends SessionProvider
        implements BankParamsRepository
{
    @Override
    public void create(BankParams bankParams)
    {
        getCurrentSession().saveOrUpdate(bankParams);
    }

    @Override
    public BankParams getById(Integer id)
    {
        Session session = getCurrentSession();
        return (BankParams) session.get(BankParams.class, id);
    }

    @Override
    public void update(BankParams bankParams)
    {
        Session session = getCurrentSession();
        session.saveOrUpdate(bankParams);
    }

    @Override
    public BankParams getLast()
    {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(BankParams.class);
        criteria.addOrder(Order.desc("id"));
        if(criteria.list().size() != 0)
            return  (BankParams)criteria.list().get(0);
        else
            return null;
    }
}
