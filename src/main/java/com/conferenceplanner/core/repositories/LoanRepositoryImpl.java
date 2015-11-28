package com.conferenceplanner.core.repositories;

import com.conferenceplanner.core.domain.Customer;
import com.conferenceplanner.core.domain.Loan;
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
public class LoanRepositoryImpl extends SessionProvider
    implements LoanRepository
{
    @Override
    public void create(Loan loan)
    {
        getCurrentSession().saveOrUpdate(loan);
    }

    @Override
    public void update(Loan loan)
    {
        getCurrentSession().saveOrUpdate(loan);
    }

    @Override
    public Loan getById(Integer id)
    {
        return (Loan) getCurrentSession().get(Loan.class, id);
    }

    @Override
    public List<Loan> getByCustomer(Customer customer)
    {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(Loan.class);
        criteria.add(Restrictions.eq("customer", customer));
        return  (List<Loan>)criteria.list();

    }

    @Override
    public Loan getLast()
    {
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(Loan.class);
        criteria.addOrder(Order.desc("id"));
        if(criteria.list().size() != 0)
            return  (Loan)criteria.list().get(0);
        else
            return null;
    }
}
