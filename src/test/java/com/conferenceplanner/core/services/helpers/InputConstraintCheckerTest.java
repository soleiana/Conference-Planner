package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.BankParamsRepository;
import com.conferenceplanner.core.repositories.CustomerRepository;
import com.conferenceplanner.core.repositories.LoanRequestRepository;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Ann on 08/10/14.
 */
public class InputConstraintCheckerTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BankParamsRepository bankParamsRepository;
    @Autowired
    private InputConstraintChecker checker;

    private BankParams bankParams;
    private LoanRequest loanRequest;

    private void createLoanRequest(java.sql.Date submissionDate)
    {
        Customer customer = CustomerFixture.standardCustomer();
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loanRequest.setSubmissionDate(submissionDate);
        customerRepository.create(customer);
        loanRequest.setCustomer(customer);
        loanRequestRepository.create(loanRequest);
    }

    @Before
    @Transactional
    public void setUp()
    {
        dbCleaner.clear();
        bankParams = BankParamsFixture.standardBankParams();
        bankParamsRepository.create(bankParams);

        createLoanRequest(LoanRequestFixture.SUBMISSION_DATE);
    }
    @Test
    @Transactional
    public void testCheckAmountConstraint()
    {
        BigDecimal maxLoanAmount = bankParams.getMaxLoanAmount();
        loanRequest.setAmount(maxLoanAmount);

        assertTrue(checker.checkAmountConstraint(loanRequest));

        BigDecimal loanAmount = maxLoanAmount.add(new BigDecimal(1.00));
        loanRequest.setAmount(loanAmount);

        assertTrue(!checker.checkAmountConstraint(loanRequest));

        loanAmount = maxLoanAmount.subtract(new BigDecimal(1.00));
        loanRequest.setAmount(loanAmount);

        assertTrue(checker.checkAmountConstraint(loanRequest));
    }
}
