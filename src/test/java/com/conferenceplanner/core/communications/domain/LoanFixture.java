package com.conferenceplanner.core.communications.domain;

import com.conferenceplanner.core.communications.loans.domain.Loan;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Ann on 02/10/14.
 */
public class LoanFixture
{
    private static final BigDecimal STANDARD_CURRENT_INTEREST_RATE = new BigDecimal("100.00");
    private static final BigDecimal STANDARD_CURRENT_INTEREST = new BigDecimal("200.00");
    private static final Date START_DATE = Date.valueOf("2014-09-01");
    private static final Date STANDARD_END_DATE = Date.valueOf("2014-09-30");
    private static final BigDecimal AMOUNT = new BigDecimal("500.00");
    private static final Integer LOAN_ID = 1;

    public static Loan standardLoan()
    {
        Loan loan = new Loan();
        loan.setCurrInterest(STANDARD_CURRENT_INTEREST);
        loan.setCurrInterestRate(STANDARD_CURRENT_INTEREST_RATE);
        loan.setStartDate(START_DATE);
        loan.setEndDate(STANDARD_END_DATE);
        loan.setId(LOAN_ID);
        loan.setAmount(AMOUNT);
        return loan;
    }
}
