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
public class LoanExtensionRepositoryImplTest  extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private LoanExtensionRepository loanExtensionRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private LoanRepository loanRepository;

    private LoanExtension loanExtension;
    private Customer customer;
    private LoanRequest loanRequest;
    private Loan loan;

    private void createLoanRequest()
    {
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loanRequest.setCustomer(customer);
        loanRequestRepository.create(loanRequest);
    }

    private void createLoan()
    {
        createLoanRequest();
        loan = LoanFixture.standardLoan();
        loan.setCustomer(customer);
        loan.setLoanRequest(loanRequest);
        loanRepository.create(loan);
    }

    private void createLoanExtension()
    {
        createLoan();
        loanExtension = LoanExtensionFixture.standardLoanExtension();
        loanExtension.setLoan(loan);
    }

    @Before
    @Transactional
    public void setUp()
    {
        dbCleaner.clear();

        customer = CustomerFixture.standardCustomer();
        customerRepository.create(customer);

        createLoanExtension();
    }

    @Test
    @Transactional
    public void testCreate()
    {
        loanExtensionRepository.create(loanExtension);
        assertNotNull(loanExtension.getId());
    }

    @Test
    @Transactional
    public void testGetByLoan()
    {
        createLoan();
        LoanExtension loanExtension1 = LoanExtensionFixture.standardLoanExtension();
        loanExtension1.setLoan(loan);
        LoanExtension loanExtension2 = LoanExtensionFixture.standardLoanExtension();
        loanExtension2.setLoan(loan);
        loanExtensionRepository.create(loanExtension1);
        loanExtensionRepository.create(loanExtension2);

        List<LoanExtension> loanExtensions = loanExtensionRepository.getByLoan(loan);
        assertEquals(2, loanExtensions.size());
    }

}
