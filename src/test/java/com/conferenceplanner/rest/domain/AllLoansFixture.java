package com.conferenceplanner.rest.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ann on 26/09/14.
 */
public class AllLoansFixture
{
    private static final String NAME = "James";
    private static final String SURNAME = "Bond";
    private static final Integer CUSTOMER_ID = 2;
    private static final Integer LOAN_ID = 5;
    private static final BigDecimal CURR_INTEREST_RATE = new BigDecimal("150");
    private static final BigDecimal CURR_INTEREST = new BigDecimal("600");
    private static final BigDecimal INTEREST_RATE = CURR_INTEREST_RATE;
    private static final BigDecimal INTEREST = CURR_INTEREST;
    private static final BigDecimal AMOUNT = new BigDecimal("500");
    private static final Date LOAN_START_DATE = Date.valueOf("2014-09-01");
    private static final Date LOAN_END_DATE = Date.valueOf("2014-09-20");
    private static final Date LOAN_EXTENSION_START_DATE = Date.valueOf("2014-09-10");
    private static final Date LOAN_EXTENSION_END_DATE = LOAN_END_DATE;
    private static final Date LOAN_EXTENSION_SUBMISSION_DATE = LOAN_EXTENSION_START_DATE;

    private static  Loan standardLoan()
    {
        Loan loan = new Loan();
        loan.setId(LOAN_ID);
        loan.setCurrInterestRate(CURR_INTEREST_RATE);
        loan.setCurrInterest(CURR_INTEREST);
        loan.setAmount(AMOUNT);
        loan.setStartDate(LOAN_START_DATE);
        loan.setEndDate(LOAN_END_DATE);
        return  loan;
    }

    private static LoanExtension standardLoanExtension()
    {
        LoanExtension loanExtension = new LoanExtension();
        loanExtension.setLoanId(LOAN_ID);
        loanExtension.setInterestRate(INTEREST_RATE);
        loanExtension.setInterest(INTEREST);
        loanExtension.setStartDate(LOAN_EXTENSION_START_DATE);
        loanExtension.setEndDate(LOAN_EXTENSION_END_DATE);
        loanExtension.setSubmissionDate(LOAN_EXTENSION_SUBMISSION_DATE);
        return loanExtension;
    }
    public static AllLoans standardAllLoans()
    {
        AllLoans allLoans = new AllLoans();
        allLoans.setCustomerId(CUSTOMER_ID);
        allLoans.setName(NAME);
        allLoans.setSurname(SURNAME);

        Loan loan = standardLoan();
        List<LoanExtension> loanExtensions = new ArrayList<>();
        loanExtensions.add(standardLoanExtension());
        loan.setLoanExtensions(loanExtensions);

        List<Loan> loans = new ArrayList<>();
        loans.add(loan);
        allLoans.setLoans(loans);
        return allLoans;
    }
}
