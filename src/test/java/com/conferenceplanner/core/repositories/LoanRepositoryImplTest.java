package com.conferenceplanner.core.repositories;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Ann on 10/09/14.
 */
public class LoanRepositoryImplTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private LoanRepository loanRepository;

    private Customer customer;
    private LoanRequest loanRequest;
    private Loan loan;

    private void createLoan()
    {
        loan = LoanFixture.standardLoan();
        loan.setCustomer(customer);

        createLoanRequest();
        loanRequestRepository.create(loanRequest);
        loan.setLoanRequest(loanRequest);
    }

    private void createLoanRequest()
    {
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loanRequest.setCustomer(customer);

    }

    @Before
    @Transactional
    public void setUp()
    {
        dbCleaner.clear();

        customer = CustomerFixture.standardCustomer();
        customerRepository.create(customer);
        createLoan();
    }

    @Test
    @Transactional
    public void testCreate()
    {
        loanRepository.create(loan);
        assertNotNull(loan.getId());
    }

    @Test
    @Transactional
    public void testUpdate()
    {
        loanRepository.create(loan);
        loan.setCurrInterestRate(LoanFixture.NEW_CURRENT_INTEREST_RATE);
        loan.setCurrInterest(LoanFixture.NEW_CURRENT_INTEREST);
        loan.setEndDate(LoanFixture.NEW_END_DATE);

        loanRepository.update(loan);

        assertEquals(LoanFixture.NEW_CURRENT_INTEREST_RATE, loan.getCurrInterestRate());
        assertEquals(LoanFixture.NEW_CURRENT_INTEREST, loan.getCurrInterest());
        assertEquals(LoanFixture.NEW_END_DATE, loan.getEndDate());
    }

    @Test
    @Transactional
    public void testGetById()
    {
        loanRepository.create(loan);
        Loan ln = loanRepository.getById(loan.getId());
        assertEquals(loan,ln);
    }

    @Test
    @Transactional
    public void testGetByCustomer()
    {
        createLoan();
        loanRepository.create(loan);
        createLoan();
        loanRepository.create(loan);
        List<Loan> loans = loanRepository.getByCustomer(customer);
        assertEquals(2, loans.size());
    }
}
