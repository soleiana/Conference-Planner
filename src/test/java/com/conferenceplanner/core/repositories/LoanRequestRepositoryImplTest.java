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
public class LoanRequestRepositoryImplTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private LoanRequest loanRequest;
    private Customer customer;
    private String requestIp;

    private void createLoanRequest()
    {
        loanRequest = LoanRequestFixture.standardLoanRequest();
        loanRequest.setCustomer(customer);
        loanRequest.setRequestIp(requestIp);
    }

    @Before
    @Transactional
    public void setUp()
    {
        dbCleaner.clear();
        requestIp = RequestIPFixture.IP;
        customer = CustomerFixture.standardCustomer();
        customerRepository.create(customer);
        createLoanRequest();
    }

    @Test
    @Transactional
    public void testCreate()
    {
        loanRequestRepository.create(loanRequest);
        assertNotNull(loanRequest.getId());
    }

    @Test
    @Transactional
    public void testGetByRequestIp()
    {
        createLoanRequest();
        loanRequestRepository.create(loanRequest);
        createLoanRequest();
        loanRequestRepository.create(loanRequest);
        List<LoanRequest> loanRequests = loanRequestRepository.getByRequestIp(requestIp);
        assertEquals(2, loanRequests.size());
    }
}
