package com.conferenceplanner.core.communications.loans;

import com.conferenceplanner.core.communications.CreateEntityResponse;
import com.conferenceplanner.core.communications.DomainResponse;

/**
 * Created by Ann on 06/09/14.
 */
public class CreateLoanExtensionResponse extends CreateEntityResponse
    implements DomainResponse
{
    private final String message;

    public CreateLoanExtensionResponse(Boolean loanExtensionCreated, String message)
    {
        super.created = loanExtensionCreated;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
