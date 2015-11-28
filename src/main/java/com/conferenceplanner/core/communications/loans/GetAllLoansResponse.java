package com.conferenceplanner.core.communications.loans;

import com.conferenceplanner.core.communications.DomainResponse;
import com.conferenceplanner.core.communications.GetEntityResponse;
import com.conferenceplanner.core.communications.loans.domain.AllLoansDetails;

/**
 * Created by Ann on 06/09/14.
 */
public class GetAllLoansResponse extends GetEntityResponse
    implements DomainResponse
{
    private final AllLoansDetails allLoansDetails;

    public GetAllLoansResponse(AllLoansDetails allLoansDetails, Boolean entityFound)
    {
        this.allLoansDetails = allLoansDetails;
        super.entityFound = entityFound;
    }

    public AllLoansDetails getAllLoansDetails()
    {
        return allLoansDetails;
    }
}
