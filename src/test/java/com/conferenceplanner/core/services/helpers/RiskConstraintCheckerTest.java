package com.conferenceplanner.core.services.helpers;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.*;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import com.conferenceplanner.core.services.common.DateTimeUtility;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertNotNull;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Ann on 13/09/14.
 */
public class RiskConstraintCheckerTest extends SpringContextTest
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
    private RiskConstraintChecker checker;

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
    public void testCheckMaxRequestsPerIP()
    {
        bankParams.setMaxLoanAttempts(new Byte("3"));
        bankParamsRepository.update(bankParams);

        Date now = new Date();
        java.sql.Date sqlNow = DateTimeUtility.getSqlDate(now);

        assertNotNull(sqlNow);

        createLoanRequest(sqlNow);
        assertTrue(checker.checkMaxRequestsPerIP(loanRequest));
        createLoanRequest(sqlNow);
        assertTrue(checker.checkMaxRequestsPerIP(loanRequest));
        createLoanRequest(sqlNow);
        assertTrue(!checker.checkMaxRequestsPerIP(loanRequest));
    }

    @Test
    @Transactional
    public void testCheckTimeConstraint_1()
    {
        //check loan requests for risk interval [00:00:00-07:00:00]

        Time riskTimeStart = bankParams.getRiskTimeStart();
        Time riskTimeEnd = bankParams.getRiskTimeEnd();

        //loan request in risk interval
        Time submissionTime = DateTimeUtility.increaseTime(riskTimeStart,1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(!checker.checkTimeConstraint(loanRequest));

        submissionTime = DateTimeUtility.increaseTime(riskTimeEnd,-1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(!checker.checkTimeConstraint(loanRequest));

        //loan request not in risk interval
        submissionTime = DateTimeUtility.increaseTime(riskTimeStart,-1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(checker.checkTimeConstraint(loanRequest));

        submissionTime = DateTimeUtility.increaseTime(riskTimeEnd,1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(checker.checkTimeConstraint(loanRequest));
    }

    @Test
    @Transactional
    public void testCheckTimeConstraint_2()
    {
        //check loan requests for risk interval [22:00:00-08:00:00]

        BankParams newBankParams = BankParamsFixture.newBankParams();
        bankParamsRepository.create(newBankParams);

        Time riskTimeStart = newBankParams.getRiskTimeStart();
        Time riskTimeEnd = newBankParams.getRiskTimeEnd();

        //loan request in risk interval
        Time submissionTime = DateTimeUtility.increaseTime(riskTimeStart,1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(!checker.checkTimeConstraint(loanRequest));

        //loan request not in risk interval
        submissionTime = DateTimeUtility.increaseTime(riskTimeEnd,1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(checker.checkTimeConstraint(loanRequest));

        submissionTime = DateTimeUtility.increaseTime(riskTimeStart,-1);
        loanRequest.setSubmissionTime(submissionTime);
        assertTrue(checker.checkTimeConstraint(loanRequest));
    }

    @Test
    @Transactional
    public void testIsMaxAmount()
    {
        BigDecimal maxLoanAmount = bankParams.getMaxLoanAmount();
        loanRequest.setAmount(maxLoanAmount);

        assertTrue(checker.isMaxAmount(loanRequest));

        BigDecimal loanAmount = maxLoanAmount.subtract(new BigDecimal(1.00));
        loanRequest.setAmount(loanAmount);

        assertTrue(!checker.isMaxAmount(loanRequest));

        loanAmount = maxLoanAmount.add(new BigDecimal(1.00));
        loanRequest.setAmount(loanAmount);

        assertTrue(!checker.isMaxAmount(loanRequest));
    }
}
