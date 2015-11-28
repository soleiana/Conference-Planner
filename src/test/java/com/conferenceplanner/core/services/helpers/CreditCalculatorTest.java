package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.BankParamsRepository;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

/**
 * Created by Ann on 13/09/14.
 */
public class CreditCalculatorTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private BankParamsRepository bankParamsRepository;
    @Autowired
    private CreditCalculator creditCalculator;

    private BankParams bankParams;
    private LoanRequest loanRequest;
    private Loan loan;

    @Before
    @Transactional
    public void setUp()
    {
        dbCleaner.clear();
        bankParams = BankParamsFixture.standardBankParams();
        bankParamsRepository.create(bankParams);
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loan = LoanFixture.standardLoan();
        loan.setLoanRequest(loanRequest);
    }

    @Test
    @Transactional
    public void testGetLoanEndDate()
    {
        Date submissionDate = Date.valueOf("2014-09-01");
        loanRequest.setSubmissionDate(submissionDate);
        loanRequest.setTerm(20);

        Date endDate = creditCalculator.getLoanEndDate(loanRequest);
        assertEquals(Date.valueOf("2014-09-21"),endDate);
    }

    @Test
    @Transactional
    public void testGetInterest_1()
    {
        BigDecimal amount = new BigDecimal("200.00");
        Integer term = 20;
        BigDecimal t = new BigDecimal("20");
        BigDecimal interestRate = bankParams.getBaseInterestRate();
        BigDecimal expectedInterest = amount.multiply(interestRate)
                                            .multiply(t)
                                            .multiply(creditCalculator.FACTOR);
        expectedInterest = expectedInterest.setScale(2, RoundingMode.HALF_EVEN);
        loanRequest.setAmount(amount);
        loanRequest.setTerm(term);

        BigDecimal interest = creditCalculator.getInterest(loanRequest);
        assertTrue(interest.compareTo(expectedInterest) == 0);
    }

    @Test
    @Transactional
    public void testGetInterest_2()
    {
        BigDecimal amount = new BigDecimal("200.00");
        Integer term = 20;
        BigDecimal interestRate = new BigDecimal("100.00");
        loan.getLoanRequest().setAmount(amount);
        loan.getLoanRequest().setTerm(term);
        loan.setCurrInterestRate(interestRate);

        BigDecimal interest = creditCalculator.getInterest(loan);
        assertTrue(interest.compareTo(new BigDecimal("22.50")) == 0);
    }

    @Test
    @Transactional
    public void testGetLoanExtensionEndDate()
    {
        Date loanEndDate = Date.valueOf("2014-09-01");
        loan.setEndDate(loanEndDate);

        Date loanExtensionEndDate = creditCalculator.getLoanExtensionEndDate(loan);
        assertEquals(Date.valueOf("2014-09-08"),loanExtensionEndDate);
    }
}
