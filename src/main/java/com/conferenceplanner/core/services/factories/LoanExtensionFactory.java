package com.conferenceplanner.core.services.factories;

import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanExtension;
import com.conferenceplanner.core.repositories.LoanRepository;
import com.conferenceplanner.core.services.common.DateTimeUtility;
import com.conferenceplanner.core.services.helpers.CreditCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Ann on 14/09/14.
 */
@Component
public class LoanExtensionFactory
{
    @Autowired
    private CreditCalculator creditCalculator;
    @Autowired
    private LoanRepository loanRepository;


    public LoanExtension getNewLoanExtension(Integer loanId)
    {
        LoanExtension loanExtension = new LoanExtension();

        Loan loan = loanRepository.getById(loanId);

        loanExtension.setLoan(loan);

        Date dNow = new Date();
        java.sql.Date submissionDate = DateTimeUtility.getSqlDate(dNow);
        loanExtension.setSubmissionDate(submissionDate);

        loanExtension.setStartDate(loan.getEndDate());
        java.sql.Date endDate = creditCalculator.getLoanExtensionEndDate(loan);
        loanExtension.setEndDate(endDate);

        BigDecimal interestRate = creditCalculator.getNewInterestRate(loan);
        loanExtension.setInterestRate(interestRate);
        BigDecimal interest = creditCalculator.getInterest(loan);
        loanExtension.setInterest(interest);

        return  loanExtension;
    }
}
