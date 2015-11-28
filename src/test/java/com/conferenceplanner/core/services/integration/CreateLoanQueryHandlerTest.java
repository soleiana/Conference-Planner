package com.conferenceplanner.core.services.integration;

import com.conferenceplanner.SpringContextTest;
import com.conferenceplanner.core.communications.domain.LoanRequestDetailsFixture;
import com.conferenceplanner.core.communications.loans.CreateLoanQuery;
import com.conferenceplanner.core.communications.loans.CreateLoanResponse;
import com.conferenceplanner.core.communications.loans.domain.LoanRequestDetails;
import com.conferenceplanner.core.domain.*;
import com.conferenceplanner.core.repositories.*;
import com.conferenceplanner.core.repositories.tools.DBCleaner;
import com.conferenceplanner.core.services.CreateLoanQueryHandler;
import com.conferenceplanner.core.services.common.Message;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

/**
 * Created by Ann on 02/10/14.
 */
public class CreateLoanQueryHandlerTest extends SpringContextTest
{
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private LoanRequestRepository loanRequestRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BankParamsRepository bankParamsRepository;
    @Autowired
    private CreateLoanQueryHandler createLoanQueryHandler;

    private BankParams bankParams;
    private Customer customer;

    private CreateLoanQuery createCreateLoanQuery()
    {
        LoanRequestDetails loanRequestDetails =
                LoanRequestDetailsFixture.standardLoanRequestDetails();
        loanRequestDetails.setCustomerId(customer.getId());

        return new CreateLoanQuery(loanRequestDetails);
    }

    @Before
    public void setUp()
    {
        dbCleaner.clear();

        bankParams = BankParamsFixture.standardBankParams();
        bankParamsRepository.create(bankParams);
        customer = CustomerFixture.standardCustomer();
        customerRepository.create(customer);
    }

    @Test
    @Transactional
    public void testExecute()
    {
        CreateLoanQuery createLoanQuery = createCreateLoanQuery();
        CreateLoanResponse expectedCreateLoanResponse =
                new CreateLoanResponse(true, Message.LOAN_OBTAINED_MESSAGE);

        LoanRequest loanRequest1 = loanRequestRepository.getLast();
        Loan loan1 = loanRepository.getLast();
        assertNull(loanRequest1);
        assertNull(loan1);

        CreateLoanResponse createLoanResponse = createLoanQueryHandler.execute(createLoanQuery);

        assertEquals(expectedCreateLoanResponse.getMessage(), createLoanResponse.getMessage());
        assertEquals(expectedCreateLoanResponse.isCreated(), createLoanResponse.isCreated());

        loanRequest1 = loanRequestRepository.getLast();
        loan1 = loanRepository.getLast();

        assertNotNull(loanRequest1);
        assertNotNull(loan1);
        assertEquals(customer.getId(), loan1.getCustomer().getId());
        assertEquals(customer.getId(), loanRequest1.getCustomer().getId());
    }
}
