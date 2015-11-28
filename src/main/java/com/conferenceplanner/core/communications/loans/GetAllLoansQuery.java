package com.conferenceplanner.core.communications.loans;

import com.conferenceplanner.core.communications.DomainQuery;
import com.conferenceplanner.core.communications.GetEntityQuery;

/**
 * Created by Ann on 06/09/14.
 */
public class GetAllLoansQuery extends GetEntityQuery
    implements DomainQuery<GetAllLoansResponse>
{
    private final Integer customerId;

    public GetAllLoansQuery(Integer customerId)
    {
        this.customerId = customerId;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }
}
