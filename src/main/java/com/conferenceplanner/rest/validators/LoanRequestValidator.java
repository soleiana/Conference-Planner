package com.conferenceplanner.rest.validators;

import com.conferenceplanner.rest.domain.LoanRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Ann on 17/09/14.
 */

@Component
public class LoanRequestValidator
{
    public boolean validate(LoanRequest loanRequest)
    {
        Integer customerId = loanRequest.getCustomerId();
        BigDecimal amount = loanRequest.getAmount();
        Integer term = loanRequest.getTerm();

        if((customerId == null)|| (amount == null) || (term == null))
            return false;

        if ((customerId <= 0)||
            (amount.compareTo(new BigDecimal("0.00")) <= 0)||
            (term <= 0))
            return false;
        return true;
    }
}
