package com.conferenceplanner.core.repositories.tools;

import com.conferenceplanner.core.repositories.SessionProvider;
import org.hibernate.Query;
import org.springframework.stereotype.Component;

/**
 * Created by Ann on 12/09/14.
 */
@Component
public class ORMCleanerImpl extends SessionProvider
        implements  DBCleaner
{
    public void clear()
    {
        hqlTruncate("LoanExtension");
        hqlTruncate("Loan");
        hqlTruncate("LoanRequest");
        hqlTruncate("Customer");
    }

    public int hqlTruncate(String myTable){
        String hql = String.format("delete from %s",myTable);
        Query query = getCurrentSession().createQuery(hql);
        return query.executeUpdate();
    }
}
