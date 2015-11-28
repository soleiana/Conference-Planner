package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.core.domain.LoanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Ann on 12/09/14.
 */
@Component
public class CreditExpert
{
    @Autowired
    private RiskConstraintChecker checker;

    public boolean hasRisks(LoanRequest loanRequest)
    {
        if (!checker.checkMaxRequestsPerIP(loanRequest)||
            (checker.isMaxAmount(loanRequest)&&!checker.checkTimeConstraint(loanRequest)))
           return true;
        else
           return false;
    }
}
