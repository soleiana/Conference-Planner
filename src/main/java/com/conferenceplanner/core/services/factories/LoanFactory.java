package com.conferenceplanner.core.services.factories;

import com.conferenceplanner.core.domain.BankParams;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanExtension;
import com.conferenceplanner.core.domain.LoanRequest;
import com.conferenceplanner.core.repositories.BankParamsRepository;
import com.conferenceplanner.core.repositories.LoanRepository;
import com.conferenceplanner.core.services.helpers.CreditCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Ann on 12/09/14.
 */
@Component
public class LoanFactory
{
    @Autowired
    private CreditCalculator creditCalculator;
    @Autowired
    private BankParamsRepository bankParamsRepository;
    @Autowired
    private LoanRepository loanRepository;

    public Loan getNewLoan(LoanRequest loanRequest)
    {
        Loan loan = new Loan();
        loan.setLoanRequest(loanRequest);
        loan.setCustomer(loanRequest.getCustomer());
        loan.setStartDate(loanRequest.getSubmissionDate());

        Date endDate = creditCalculator.getLoanEndDate(loanRequest);
        loan.setEndDate(endDate);
        BigDecimal interest = creditCalculator.getInterest(loanRequest);
        loan.setCurrInterest(interest);

        BankParams bankParams = bankParamsRepository.getLast();
        loan.setCurrInterestRate(bankParams.getBaseInterestRate());
        return loan;
    }

    public Loan getExtendedLoan(LoanExtension loanExtension)
    {
        Integer id = loanExtension.getLoan().getId();
        Loan loan = loanRepository.getById(id);
        loan.setCurrInterest(loanExtension.getInterestRate());
        loan.setCurrInterest(loanExtension.getInterest());
        loan.setEndDate(loanExtension.getEndDate());
        return  loan;
    }
}
