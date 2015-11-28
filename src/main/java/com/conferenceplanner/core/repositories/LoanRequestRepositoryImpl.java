package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.LoanRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Ann on 10/09/14.
 */
@Component
public class LoanRequestRepositoryImpl extends SessionProvider
    implements LoanRequestRepository
{
    @Override
    public void create(LoanRequest loanRequest)
    {
        getCurrentSession().saveOrUpdate(loanRequest);
    }

    @Override
    public List<LoanRequest> getByRequestIp(String requestIp)
    {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(LoanRequest.class);
        criteria.add(Restrictions.eq("requestIp", requestIp));
        return  (List<LoanRequest>)criteria.list();
    }

    @Override
    public LoanRequest getLast()
    {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(LoanRequest.class);
        criteria.addOrder(Order.desc("id"));
        if(criteria.list().size() != 0)
            return  (LoanRequest)criteria.list().get(0);
        else
            return null;
    }
}
