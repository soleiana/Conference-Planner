package com.conferenceplanner.core.domain;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by Ann on 10/09/14.
 */
public class LoanFixture
{
    private static final BigDecimal STANDARD_CURRENT_INTEREST_RATE = new BigDecimal("100.00");
    private static final BigDecimal STANDARD_CURRENT_INTEREST = new BigDecimal("200.00");
    private static final Date START_DATE = Date.valueOf("2014-09-01");
    private static final Date STANDARD_END_DATE = Date.valueOf("2014-09-30");

    public static final BigDecimal NEW_CURRENT_INTEREST_RATE = new BigDecimal("150.00");
    public static final BigDecimal NEW_CURRENT_INTEREST = new BigDecimal("300.00");
    public static final Date NEW_END_DATE = Date.valueOf("2014-10-06");

    public static Loan standardLoan()
    {
        Loan loan = new Loan();
        loan.setCurrInterest(STANDARD_CURRENT_INTEREST);
        loan.setCurrInterestRate(STANDARD_CURRENT_INTEREST_RATE);
        loan.setStartDate(START_DATE);
        loan.setEndDate(STANDARD_END_DATE);
        return loan;
    }
}
