package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.core.domain.BankParams;
import com.conferenceplanner.core.domain.Loan;
import com.conferenceplanner.core.domain.LoanRequest;
import com.conferenceplanner.core.repositories.BankParamsRepository;
import com.conferenceplanner.core.services.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;


/**
 * Created by Ann on 12/09/14.
 */
@Component
public class CreditCalculator
{
    public static final BigDecimal FACTOR = new BigDecimal("0.0000277777");

    @Autowired
    private BankParamsRepository bankParamsRepository;

    private BigDecimal interestFormula(BigDecimal amount,
                                       BigDecimal term,
                                       BigDecimal interestRate)
    {
        BigDecimal factor = amount.multiply(interestRate)
                .multiply(term);

        BigDecimal interest = factor.multiply(FACTOR);
        interest = interest.setScale(2, RoundingMode.HALF_EVEN);
        return interest;
    }

    public Date getLoanEndDate(LoanRequest loanRequest)
    {
        Date startDate = loanRequest.getSubmissionDate();
        Integer term = loanRequest.getTerm();

        Date endDate = DateTimeUtility.increaseDate(startDate,term);
        return endDate;
    }

    public Date getLoanExtensionEndDate(Loan loan)
    {
        Date startDate = loan.getEndDate();
        short loanExtensionTerm = bankParamsRepository.getLast().getLoanExtensionTerm();
        Date endDate = DateTimeUtility.increaseDate(startDate, (int)loanExtensionTerm);
        return endDate;
    }

    public BigDecimal getInterest(LoanRequest loanRequest)
    {
        BankParams bankParams = bankParamsRepository.getLast();
        BigDecimal baseInterestRate = bankParams.getBaseInterestRate();
        BigDecimal amount = loanRequest.getAmount();
        BigDecimal term = new BigDecimal(loanRequest.getTerm());
        return interestFormula(amount,term,baseInterestRate);
    }

    public BigDecimal getInterest(Loan loan)
    {
       BigDecimal amount = loan.getLoanRequest().getAmount();
       BigDecimal currInterestRate = getNewInterestRate(loan);

       short loanExtensionTerm = bankParamsRepository.getLast().getLoanExtensionTerm();
       int term = loan.getLoanRequest().getTerm();
       term += loanExtensionTerm;
       return  interestFormula(amount, new BigDecimal(term),currInterestRate);
    }

    public BigDecimal getNewInterestRate(Loan loan)
    {
        BankParams bankParams = bankParamsRepository.getLast();
        BigDecimal interestRateFactor = bankParams.getInterestRateFactor();
        BigDecimal currInterestRate = loan.getCurrInterestRate();
        currInterestRate = currInterestRate.multiply(interestRateFactor);
        currInterestRate = currInterestRate.setScale(2, RoundingMode.HALF_EVEN);
        return currInterestRate;
    }
}
