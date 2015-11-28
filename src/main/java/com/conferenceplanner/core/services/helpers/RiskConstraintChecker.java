package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.core.domain.BankParams;
import com.conferenceplanner.core.domain.LoanRequest;
import com.conferenceplanner.core.repositories.LoanRequestRepository;
import com.conferenceplanner.core.services.common.DateTimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Ann on 13/09/14.
 */
@Component
public class RiskConstraintChecker extends ConstraintChecker
{

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    private boolean isBetween(Time start, Time end, Time timeToCheck)
    {
        if ((timeToCheck.compareTo(start) == 1)&&(end.compareTo(timeToCheck) == 1))
            return true;

        else
            return false;
    }

    public boolean checkMaxRequestsPerIP(LoanRequest loanRequest)
    {
        BankParams bankParams = getBankParams();
        Byte maxLoanAttempts = bankParams.getMaxLoanAttempts();

        String requestIp = loanRequest.getRequestIp();
        List<LoanRequest> loanRequests = loanRequestRepository.getByRequestIp(requestIp);

        Date now = new Date();
        java.sql.Date sqlNow = DateTimeUtility.getSqlDate(now);

        int requestNum = 0;

        for(LoanRequest req: loanRequests)
            if (req.getSubmissionDate().equals(sqlNow))
                requestNum++;

        if (requestNum < maxLoanAttempts)
            return true;
        else
            return false;
    }

    public boolean checkTimeConstraint(LoanRequest loanRequest)
    {
        BankParams bankParams = getBankParams();
        Time riskTimeStart = bankParams.getRiskTimeStart();
        Time riskTimeEnd = bankParams.getRiskTimeEnd();

        Time submissionTime = loanRequest.getSubmissionTime();

        if (riskTimeStart.compareTo(riskTimeEnd) == 1)
            //check two time intervals: [riskTimeStart, 24:00:00]
            //and [00:00:00, riskTimeEnd]
            if ( isBetween(riskTimeStart, DateTimeUtility.MAX_TIME, submissionTime) ||
                    isBetween(DateTimeUtility.MIN_TIME, riskTimeEnd, submissionTime)
                    )
                return false;
            else
                return true;
        else
            //check one time interval: [riskTimeStart, riskTimeEnd]
            if (isBetween(riskTimeStart, riskTimeEnd, submissionTime))
                return false;
            else
                return  true;

    }

    public boolean isMaxAmount(LoanRequest loanRequest)
    {
        BankParams bankParams = getBankParams();
        BigDecimal maxLoanAmount = bankParams.getMaxLoanAmount();
        BigDecimal reqAmount = loanRequest.getAmount();

        if(reqAmount.compareTo(maxLoanAmount) == 0)
            return true;
        else
            return false;
    }
}
