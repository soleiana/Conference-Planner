package com.conferenceplanner.core.communications.loans;

import com.conferenceplanner.core.communications.CreateEntityQuery;
import com.conferenceplanner.core.communications.DomainQuery;
import com.conferenceplanner.core.communications.loans.domain.LoanRequestDetails;

/**
 * Created by Ann on 06/09/14.
 */
public class CreateLoanQuery extends CreateEntityQuery
        implements DomainQuery<CreateLoanResponse>
{
    private final LoanRequestDetails loanRequestDetails;

    public CreateLoanQuery(LoanRequestDetails loanRequestDetails)
    {
        this.loanRequestDetails = loanRequestDetails;
    }

    public LoanRequestDetails getLoanRequestDetails()
    {
        return loanRequestDetails;

    }

}
