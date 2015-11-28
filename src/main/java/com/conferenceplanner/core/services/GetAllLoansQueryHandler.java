package com.conferenceplanner.core.services;


import com.conferenceplanner.core.communications.loans.GetAllLoansQuery;
import com.conferenceplanner.core.communications.loans.GetAllLoansResponse;
import com.conferenceplanner.core.communications.loans.domain.AllLoansDetails;
import com.conferenceplanner.core.communications.loans.factories.AllLoansDetailsFactory;
import com.conferenceplanner.core.domain.AllLoans;
import com.conferenceplanner.core.services.factories.AllLoansCoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Ann on 02/10/14.
 */
@Component
public class GetAllLoansQueryHandler
        implements QueryHandler<GetAllLoansQuery, GetAllLoansResponse>
{

    @Autowired
    private AllLoansCoreFactory allLoansCoreFactory;

        @Autowired
        private AllLoansDetailsFactory allLoansDetailsFactory;

    @Override
    public GetAllLoansResponse execute(GetAllLoansQuery query)
    {
        AllLoans allLoans;
        Integer customerId = query.getCustomerId();

        allLoans = allLoansCoreFactory.getNewAllLoans(customerId);
        AllLoansDetails allLoansDetails = allLoansDetailsFactory.getNewAllLoansDetails(allLoans);

        if ((allLoansDetails != null)&&(allLoansDetails.getLoans().size() == 0))
            return new GetAllLoansResponse(allLoansDetails,false);
        else
            return new GetAllLoansResponse(allLoansDetails,true);
    }

    @Override
    public Class getQueryType()
    {
        return GetAllLoansQuery.class;
    }
}
